package de.prob.ltl.parser.semantic;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.LoopContext;
import de.prob.ltl.parser.LtlParser.Var_assignContext;
import de.prob.ltl.parser.LtlParser.Var_defContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.symboltable.LoopTypes;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.output.StructuredPrologOutput;
import de.prob.prolog.term.IntegerPrologTerm;
import de.prob.prolog.term.PrologTerm;

public class Loop extends SymbolTable implements Node {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private LoopContext context;
	private Token token;
	private LoopTypes type;
	private Variable countVariable;
	private List<VariableValue> values = new LinkedList<VariableValue>();

	public Loop(LtlParser parser, LoopContext context) {
		super(parser.getSymbolTableManager().getCurrentScope(), true);
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			symbolTableManager.pushScope(this);

			checkLoopArguments();	/* 	Check arguments before defining the counting variable
			 							Else it would be possible to use the counting variable as argument
										e.g:
										count i: 1 up to i:
										...
										end */
			determineLoopInfo();
			checkLoopBody();

			symbolTableManager.popScope();
		}
	}

	private void determineLoopInfo() {
		TerminalNode beginNode = context.LOOP_BEGIN();
		token = beginNode.getSymbol();
		type = (context.UP() != null ? LoopTypes.up : LoopTypes.down);

		TerminalNode node = context.ID();
		if (node != null) {
			String name = node.getText();
			countVariable = new Variable(name, VariableTypes.num);

			// Define variable
			if (!symbolTableManager.define(countVariable)) {
				parser.notifyErrorListeners(node.getSymbol(), String.format("The variable '%s' is already defined.", name), null);
			}
		}
	}

	private void checkLoopArguments() {
		for (int i = 0; i < context.var_value().size(); i++) {
			Var_valueContext var_value = context.var_value(i);

			VariableValue value = new VariableValue(parser, var_value);
			VariableTypes valueType = value.getValueType();
			values.add(value);

			if (!valueType.equals(VariableTypes.num)) {
				String msg = String.format("Type mismatch. Loop argument has the type '%s'. Expected type '%s'.", valueType, VariableTypes.num);
				if (value.getToken() != null) {
					parser.notifyErrorListeners(value.getToken(), msg, null);
				} else {
					// TODO create token from starttoken and stoptoken
					parser.notifyErrorListeners(msg);
				}
			}
		}
	}

	private void checkLoopBody() {
		for (ParseTree child : context.loop_body().children) {
			if (child instanceof Var_defContext) {
				// Define variable and check its initial value
				VariableDefinition definition = new VariableDefinition(parser, (Var_defContext) child);
				symbolTableManager.addNode(definition);
			} else if (child instanceof Var_assignContext) {
				// Check variable and its assigned value
				VariableAssignment assignment = new VariableAssignment(parser, (Var_assignContext) child);
				symbolTableManager.addNode(assignment);
			}
		}
	}

	@Override
	public void createPrologTerm(LtlParser parser, IPrologTermOutput pto,
			String currentState, ProBParserBase parserBase) {
		symbolTableManager.pushScope(this);
		StructuredPrologOutput startPto = new StructuredPrologOutput();
		values.get(0).createPrologTerm(parser, startPto, currentState, parserBase);
		StructuredPrologOutput endPto = new StructuredPrologOutput();
		values.get(1).createPrologTerm(parser, endPto, currentState, parserBase);
		startPto.fullstop();
		PrologTerm startTerm = startPto.getSentences().get(0);
		endPto.fullstop();
		PrologTerm endTerm = endPto.getSentences().get(0);

		if (!startTerm.isNumber() || !endTerm.isNumber()) {
			if (!startTerm.isNumber()) {
				parser.notifyErrorListeners(token, "Start value of the loop is not a number.", null);
			}
			if (!endTerm.isNumber()) {
				parser.notifyErrorListeners(token, "End value of the loop is not a number.", null);
			}
		} else {
			BigInteger start = ((IntegerPrologTerm)startTerm).getValue();
			BigInteger end = ((IntegerPrologTerm)endTerm).getValue();

			boolean isUp = type.equals(LoopTypes.up);
			int compare = (isUp ? -1 : 1);
			while (start.compareTo(end) == compare) {
				if (countVariable != null) {
					countVariable.setValue(new IntegerPrologTerm(start));
				}
				loopStep(parser, pto, currentState, parserBase);
				if (isUp) {
					start = start.add(BigInteger.ONE);
				}else {
					start = start.subtract(BigInteger.ONE);
				}
			}
		}
		symbolTableManager.popScope();
	}

	private void loopStep(LtlParser parser, IPrologTermOutput pto,
			String currentState, ProBParserBase parserBase) {
		for (Node node : symbolTableManager.getNodes()) {
			node.createPrologTerm(parser, pto, currentState, parserBase);
		}
	}

	// Getters
	public Token getToken() {
		return token;
	}

	public LoopTypes getLoopType() {
		return type;
	}

}
