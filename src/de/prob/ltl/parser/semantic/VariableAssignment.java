package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Var_assignContext;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class VariableAssignment {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private Var_assignContext context;
	private Token token;
	private String name;
	private Variable variable;

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
		VariableValue value = new VariableValue(parser, context.var_value());
		VariableTypes valueType = value.getValueType();

		if (variable != null && !variable.getType().equals(valueType)) {
			parser.notifyErrorListeners(token, String.format("Type mismatch. Right operand of variable assignment '%s' has the type '%s'.", variable, valueType), null);
		}
	}

}
