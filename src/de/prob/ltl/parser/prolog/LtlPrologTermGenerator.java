package de.prob.ltl.parser.prolog;

import java.math.BigInteger;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.NumValueContext;
import de.prob.ltl.parser.LtlParser.ParValueContext;
import de.prob.ltl.parser.LtlParser.VarValueContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.semantic.ExprOrAtom;
import de.prob.ltl.parser.semantic.Loop;
import de.prob.ltl.parser.semantic.Node;
import de.prob.ltl.parser.semantic.PatternCall;
import de.prob.ltl.parser.semantic.Variable;
import de.prob.ltl.parser.semantic.VariableAssignment;
import de.prob.ltl.parser.semantic.VariableCall;
import de.prob.ltl.parser.semantic.VariableDefinition;
import de.prob.ltl.parser.semantic.VariableValue;
import de.prob.ltl.parser.symboltable.LoopTypes;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.output.StructuredPrologOutput;
import de.prob.prolog.term.IntegerPrologTerm;
import de.prob.prolog.term.PrologTerm;

public class LtlPrologTermGenerator {

	private final LtlParser parser;
	private final String currentState;
	private final ProBParserBase parserBase;

	private final SymbolTableManager symbolTableManager;

	public LtlPrologTermGenerator(LtlParser parser, String currentState, ProBParserBase parserBase) {
		this.parser = parser;
		this.currentState = currentState;
		this.parserBase = parserBase;

		symbolTableManager = parser.getSymbolTableManager();
	}

	public void generatePrologTerm(IPrologTermOutput pto) {
		for (Node node : symbolTableManager.getNodes()) {
			if (node instanceof VariableDefinition) {
				generateVariableDefinition((VariableDefinition) node);
			} else if (node instanceof VariableAssignment) {
				generateVariableAssignment((VariableAssignment) node);
			} else if (node instanceof Loop) {
				generateLoop((Loop) node, pto);
			} else if (node instanceof ExprOrAtom) {
				generateExprOrAtom((ExprOrAtom) node, pto);
			}
		}
	}

	public void generateVariableDefinition(VariableDefinition definition) {
		StructuredPrologOutput temp = new StructuredPrologOutput();
		generateVariableValue(definition.getValue(), temp);
		temp.fullstop();
		definition.getVariable().setValue(temp.getSentences().get(0));
	}

	public void generateVariableAssignment(VariableAssignment assignment) {
		StructuredPrologOutput temp = new StructuredPrologOutput();
		generateVariableValue(assignment.getValue(), temp);
		temp.fullstop();
		assignment.getVariable().setValue(temp.getSentences().get(0));
	}

	public void generateLoop(Loop loop, IPrologTermOutput pto) {
		symbolTableManager.pushScope(loop);
		StructuredPrologOutput startPto = new StructuredPrologOutput();
		generateVariableValue(loop.getValues().get(0), startPto);
		StructuredPrologOutput endPto = new StructuredPrologOutput();
		generateVariableValue(loop.getValues().get(1), endPto);
		startPto.fullstop();
		PrologTerm startTerm = startPto.getSentences().get(0);
		endPto.fullstop();
		PrologTerm endTerm = endPto.getSentences().get(0);

		if (!startTerm.isNumber() || !endTerm.isNumber()) {
			if (!startTerm.isNumber()) {
				parser.notifyErrorListeners(loop.getToken(), "Start value of the loop is not a number.", null);
			}
			if (!endTerm.isNumber()) {
				parser.notifyErrorListeners(loop.getToken(), "End value of the loop is not a number.", null);
			}
		} else {
			BigInteger start = ((IntegerPrologTerm)startTerm).getValue();
			BigInteger end = ((IntegerPrologTerm)endTerm).getValue();

			boolean isUp = loop.getLoopType().equals(LoopTypes.up);
			int compare = (isUp ? -1 : 1);
			while (start.compareTo(end) == compare) {
				if (loop.getCountVariable() != null) {
					loop.getCountVariable().setValue(new IntegerPrologTerm(start));
				}
				generatePrologTerm(pto);
				if (isUp) {
					start = start.add(BigInteger.ONE);
				}else {
					start = start.subtract(BigInteger.ONE);
				}
			}
		}
		symbolTableManager.popScope();
	}

	public void generateExprOrAtom(ExprOrAtom expr, IPrologTermOutput epto) {
		ParseTreeWalker.DEFAULT.walk(new ExprOrAtomPrologTermGenerator(parser, this, epto, currentState, parserBase), expr.getContext());

	}

	public void generatePatternCall(PatternCall call, IPrologTermOutput epto) {
		for (int i = 0; i < call.getScopeArgumentNodes().size(); i++) {
			Variable parameter = call.getDefinition().getScopeParameters().get(i);
			ExprOrAtom argument = call.getScopeArgumentNodes().get(i);
			StructuredPrologOutput temp = new StructuredPrologOutput();
			generateExprOrAtom(argument, temp);
			temp.fullstop();
			parameter.setValue(temp.getSentences().get(0));
		}
		for (int i = 0; i < call.getArgumentNodes().size(); i++) {
			Variable parameter = call.getDefinition().getParameters().get(i);
			VariableValue argument = call.getArgumentNodes().get(i);
			StructuredPrologOutput temp = new StructuredPrologOutput();
			generateVariableValue(argument, temp);
			temp.fullstop();
			parameter.setValue(temp.getSentences().get(0));
		}

		symbolTableManager.pushScope(call.getDefinition());
		generatePrologTerm(epto);
		symbolTableManager.popScope();
	}

	public void generateVariableCall(VariableCall call, IPrologTermOutput epto) {
		epto.printTerm(call.getVariable().getValue());
	}

	public void generateVariableValue(VariableValue value, IPrologTermOutput epto) {
		Var_valueContext context= value.getContext();
		if (context instanceof VarValueContext) {
			TerminalNode node = ((VarValueContext) context).ID();
			String name = node.getText();

			Variable var = symbolTableManager.resolveVariable(name);
			epto.printTerm(var.getValue());
		} else if (context instanceof NumValueContext) {
			TerminalNode node = ((NumValueContext) context).NUM();
			String numString = node.getText();

			epto.printNumber(new BigInteger(numString));
		} else if (context instanceof ParValueContext) {
			VariableValue varValue = new VariableValue(parser, ((ParValueContext) context).var_value());
			generateVariableValue(varValue, epto);
		} else {
			ParseTreeWalker.DEFAULT.walk(new ExprOrAtomPrologTermGenerator(parser, this, epto, currentState, parserBase), context);
		}
	}

}
