package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.NumValueContext;
import de.prob.ltl.parser.LtlParser.ParValueContext;
import de.prob.ltl.parser.LtlParser.VarValueContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class VariableValue implements Node {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private Var_valueContext context;
	private Token token;
	private VariableTypes valueType;

	public VariableValue(LtlParser parser, Var_valueContext context) {
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			determineTokenAndValueType();
		}
	}

	private void determineTokenAndValueType() {
		valueType = VariableTypes.var;

		if (context instanceof VarValueContext) {
			TerminalNode node = ((VarValueContext) context).ID();
			String name = node.getText();
			token = node.getSymbol();

			Variable var = symbolTableManager.resolveVariable(name);
			if (var == null) {
				parser.notifyErrorListeners(node.getSymbol(), String.format("Variable '%s' cannot be resolved.", name), null);
			} else {
				valueType = var.getType();
			}
		} else if (context instanceof NumValueContext) {
			valueType = VariableTypes.num;
			token = ((NumValueContext) context).NUM().getSymbol();
		} else if (context instanceof ParValueContext) {
			VariableValue varValue = new VariableValue(parser, ((ParValueContext) context).var_value());
			valueType = varValue.getValueType();
			token = varValue.getToken();
		}
		// You do not have to check expr,
		// because the only problematic case could be variableCallAtom.
		// But with help of the varValue rule, this case could never be reached.
	}

	// Getters
	public VariableTypes getValueType() {
		return valueType;
	}

	public Token getToken() {
		return token;
	}

	public Var_valueContext getContext() {
		return context;
	}

}
