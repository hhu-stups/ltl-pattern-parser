package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Var_defContext;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.output.StructuredPrologOutput;

public class VariableDefinition implements Node {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private Var_defContext context;
	private Token token;
	private String name;
	private VariableTypes type;
	private Variable variable;
	private VariableValue value;

	public VariableDefinition(LtlParser parser, Var_defContext context) {
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			determineVariableInfo();

			checkInitialValue();
			// Define variable
			if (!symbolTableManager.define(variable)) {
				parser.notifyErrorListeners(token, String.format("The variable '%s' is already defined.", name), null);
			}
		}
	}

	private void determineVariableInfo() {
		TerminalNode node = context.ID();
		name = node.getText();
		token = node.getSymbol();
		type = (context.VAR() != null ? VariableTypes.var : VariableTypes.num);

		variable = new Variable(name, type);
		variable.setToken(token);
	}

	private void checkInitialValue() {
		value = new VariableValue(parser, context.var_value());
		VariableTypes valueType = value.getValueType();

		if (!type.equals(valueType)) {
			parser.notifyErrorListeners(token, String.format("Type mismatch. Right operand of variable definition '%s' has the type '%s'.", variable, valueType), null);
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
