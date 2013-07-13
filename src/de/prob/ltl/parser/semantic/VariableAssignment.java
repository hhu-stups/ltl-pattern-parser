package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Var_assignContext;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.output.StructuredPrologOutput;

public class VariableAssignment implements Node {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private Var_assignContext context;
	private Token token;
	private String name;
	private Variable variable;
	private VariableValue value;

	public VariableAssignment(LtlParser parser, Var_assignContext context) {
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			determineTokenAndName();

			// Check variable existence
			variable = symbolTableManager.resolveVariable(name);
			if (variable == null) {
				parser.notifyErrorListeners(token, String.format("Variable '%s' cannot be resolved.", name), null);
			}

			checkAssignedValue();
		}
	}

	private void determineTokenAndName() {
		TerminalNode node = context.ID();
		name = node.getText();
		token = node.getSymbol();
	}

	private void checkAssignedValue() {
		value = new VariableValue(parser, context.var_value());
		VariableTypes valueType = value.getValueType();

		if (variable != null && !variable.getType().equals(valueType)) {
			parser.notifyErrorListeners(token, String.format("Type mismatch. Right operand of variable assignment '%s' has the type '%s'.", variable, valueType), null);
		}
	}

	@Override
	public void createPrologTerm(LtlParser parser, IPrologTermOutput pto,
			String currentState, ProBParserBase parserBase) {
		StructuredPrologOutput temp = new StructuredPrologOutput();
		value.createPrologTerm(parser, temp, currentState, parserBase);
		temp.fullstop();
		variable.setValue(temp.getSentences().get(0));
	}

}
