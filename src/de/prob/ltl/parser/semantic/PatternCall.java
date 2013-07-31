package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Pattern_callContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.symboltable.SymbolTableManager;

public class PatternCall implements Node {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private Pattern_callContext context;
	private Token token;
	private String name;
	private List<Variable> arguments = new LinkedList<Variable>();
	private List<VariableValue> argumentNodes = new LinkedList<VariableValue>();
	private PatternDefinition definition;

	public PatternCall(LtlParser parser, Pattern_callContext context) {
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			determineTokenAndName();
			determineArguments();

			// Find pattern definition
			definition = symbolTableManager.resolvePattern(getName());
			if (definition == null) {
				parser.notifyErrorListeners(token, String.format("Pattern '%s' cannot be resolved.", getName()), null);
			} else {
				// Check types of arguments and parameters
				checkArguments();
			}
		}
	}

	private void determineTokenAndName() {
		TerminalNode node = context.ID();
		name = node.getText();
		token = node.getSymbol();
	}

	private void determineArguments() {
		for (Var_valueContext ctx : context.var_value()) {
			VariableValue value = new VariableValue(parser, ctx);

			Variable argument = new Variable(null, value.getValueType());
			argument.setToken(value.getToken());
			addArgument(arguments, argument);
			argumentNodes.add(value);
		}
	}

	private void checkArguments() {
		for (int i = 0; i < arguments.size(); i++) {
			Variable parameter = definition.getParameters().get(i);
			Variable argument = arguments.get(i);
			if (!parameter.getType().equals(argument.getType())) {
				String msg = String.format("Type mismatch. Passed argument has the type '%s'. Expected type '%s'.", argument.getType(), parameter.getType());
				if (argument.getToken() != null) {
					parser.notifyErrorListeners(argument.getToken(), msg, null);
				} else {
					// TODO create token from starttoken and stoptoken
					parser.notifyErrorListeners(msg);
				}
			}
		}
	}

	private void addArgument(List<Variable> argumentList, Variable var) {
		argumentList.add(var);
	}

	public String getName() {
		return PatternDefinition.createIdentifier(name, arguments);
	}

	// Getters
	public Token getToken() {
		return token;
	}

	public String getSimpleName() {
		return name;
	}

	public List<Variable> getArguments() {
		return arguments;
	}

	public PatternDefinition getDefinition() {
		return definition;
	}

	public List<VariableValue> getArgumentNodes() {
		return argumentNodes;
	}

	@Override
	public String toString() {
		return String.format("call(%s)", getName());
	}

}
