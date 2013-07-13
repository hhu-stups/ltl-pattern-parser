package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.VariableCallAtomContext;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;

public class VariableCall implements Node {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private VariableCallAtomContext context;
	private Token token;
	private String name;
	private Variable variable;

	public VariableCall(LtlParser parser, VariableCallAtomContext context) {
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			determineTokenAndName();

			checkVariable();
		}
	}

	private void determineTokenAndName() {
		TerminalNode node = context.ID();
		name = node.getText();
		token = node.getSymbol();
	}

	private void checkVariable() {
		// This variable call can only be reached in cases where it must be var type
		variable = symbolTableManager.resolveVariable(name);
		if (variable == null) {
			parser.notifyErrorListeners(token, String.format("Variable '%s' cannot be resolved.", name), null);
		} else if (!variable.getType().equals(VariableTypes.var)){
			parser.notifyErrorListeners(token, String.format("Type mismatch. Expected variable type '%s'. Found variable '%s'.", VariableTypes.var, variable), null);
		}
	}

	// Getters
	public Token getToken() {
		return token;
	}

	public String getName() {
		return name;
	}

	public Variable getVariable() {
		return variable;
	}

	@Override
	public void createPrologTerm(LtlParser parser, IPrologTermOutput pto,
			String currentState, ProBParserBase parserBase) {
		pto.printTerm(variable.getValue());
	}

}
