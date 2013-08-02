package de.prob.ltl.parser.prolog;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.prolog.scope.ScopeReplacer;
import de.prob.ltl.parser.semantic.AbstractSemanticObject;
import de.prob.ltl.parser.semantic.Argument;
import de.prob.ltl.parser.semantic.Body;
import de.prob.ltl.parser.semantic.Expr;
import de.prob.ltl.parser.semantic.Loop;
import de.prob.ltl.parser.semantic.PatternCall;
import de.prob.ltl.parser.semantic.PatternDefinition;
import de.prob.ltl.parser.semantic.ScopeCall;
import de.prob.ltl.parser.semantic.SeqCall;
import de.prob.ltl.parser.semantic.SeqDefinition;
import de.prob.ltl.parser.semantic.VariableAssignment;
import de.prob.ltl.parser.semantic.VariableDefinition;
import de.prob.ltl.parser.symboltable.LoopTypes;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.ltl.parser.symboltable.VariableTypes;
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

	public void generatePrologTerm(IPrologTermOutput pto, Body body) {
		for (AbstractSemanticObject object : body.getChildren()) {
			if (object instanceof VariableDefinition) {
				generateVariableDefinition((VariableDefinition) object, pto);
			} else if (object instanceof VariableAssignment) {
				generateVariableAssignment((VariableAssignment) object, pto);
			} else if (object instanceof Loop) {
				generateLoop((Loop) object, pto);
			} else if (object instanceof Expr) {
				generateExpr((Expr) object, pto);
			}
		}
	}

	private void generateVariableDefinition(VariableDefinition definition, IPrologTermOutput pto) {
		Variable variable = definition.getVariable();
		if (variable.getType().equals(VariableTypes.seq)) {
			variable.setSeqValue(definition.getValue().getSeq());
		} else {
			variable.setValue(generateArgument(definition.getValue()));
		}
	}

	private void generateVariableAssignment(VariableAssignment assignment, IPrologTermOutput pto) {
		Variable variable = assignment.getVariable();
		if (variable.getType().equals(VariableTypes.seq)) {
			variable.setSeqValue(assignment.getValue().getSeq());
		} else {
			variable.setValue(generateArgument(assignment.getValue()));
		}
	}

	private void generateLoop(Loop loop, IPrologTermOutput pto) {
		symbolTableManager.pushScope(loop.getSymbolTable());

		Variable variable = loop.getCounterVariable();
		PrologTerm startTerm = generateArgument(loop.getArguments().get(0));
		PrologTerm endTerm = generateArgument(loop.getArguments().get(1));
		if (!startTerm.isNumber() || !endTerm.isNumber()) {
			if (!startTerm.isNumber()) {
				parser.notifyErrorListeners(loop.getToken(), "Start value of the loop is not a number.", null);
			}
			if (!endTerm.isNumber()) {
				parser.notifyErrorListeners(loop.getToken(), "End value of the loop is not a number.", null);
			}
		} else {
			BigInteger start = ((IntegerPrologTerm) startTerm).getValue();
			BigInteger end = ((IntegerPrologTerm) endTerm).getValue();
			BigInteger count = new BigInteger(start.toByteArray());

			boolean isUp = loop.getType().equals(LoopTypes.up);
			int compare = (isUp ? -1 : 1);
			while (count.compareTo(end) == compare) {
				if (variable != null) {
					variable.setValue(new IntegerPrologTerm(count));
				}

				for (AbstractSemanticObject object : loop.getChildren()) {
					if (object instanceof VariableDefinition) {
						generateVariableDefinition((VariableDefinition) object, pto);
					} else if (object instanceof VariableAssignment) {
						generateVariableAssignment((VariableAssignment) object, pto);
					}
				}

				if (isUp) {
					count = count.add(BigInteger.ONE);
				}else {
					count = count.subtract(BigInteger.ONE);
				}
			}
		}
		symbolTableManager.popScope();
	}

	private void generateExpr(Expr expr, IPrologTermOutput pto) {
		ParseTreeWalker.DEFAULT.walk(new ExprPrologTermGenerator(parser, this, pto, currentState, parserBase), expr.getContext());
	}

	private PrologTerm generateArgument(Argument argument) {
		PrologTerm value = null;
		if (argument.getVariable() != null) {
			value = argument.getVariable().getValue();
		} else if (argument.getNum() != null) {
			value = new IntegerPrologTerm(argument.getNum());
		} else if (argument.getExpr() != null) {
			StructuredPrologOutput epto = new StructuredPrologOutput();
			generateExpr(argument.getExpr(), epto);
			epto.fullstop();
			value = epto.getSentences().get(0);
		}
		return value;
	}

	public void generatePatternCall(PatternCall call, IPrologTermOutput pto) {
		PatternDefinition definition = call.getDefinition();
		symbolTableManager.pushScope(definition.getSymbolTable());
		// set arguments
		List<Variable> parameters = definition.getParameters();
		List<Argument> arguments = call.getArguments();
		for (int i = 0; i < parameters.size(); i++) {
			Variable parameter = parameters.get(i);
			Argument argument = arguments.get(i);
			if (parameter.getType().equals(VariableTypes.seq)) {
				parameter.setSeqValue(argument.getSeq());
			} else {
				parameter.setValue(generateArgument(argument));
			}
		}

		generatePrologTerm(pto, definition.getBody());
		symbolTableManager.popScope();
	}

	public void generateScopeCall(ScopeCall call, IPrologTermOutput pto) {
		List<PrologTerm> arguments = new LinkedList<PrologTerm>();
		for (Argument arg : call.getArguments()) {
			arguments.add(generateArgument(arg));
		}

		ScopeReplacer replacer = ScopeReplacer.createReplacer(call.getType(), arguments);
		replacer.generatePrologTerm(pto);
	}

	public void generateVariableCall(String name, IPrologTermOutput pto) {
		Variable variable = symbolTableManager.resolveVariable(name);
		pto.printTerm(variable.getValue());
	}

	public void generateSeqCall(SeqCall call, IPrologTermOutput pto) {
		SeqDefinition seq = call.getSeq();
		seq.collectArguments();
	}

}
