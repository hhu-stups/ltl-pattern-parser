package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Scope_callContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.symboltable.VariableTypes;
import de.prob.prolog.term.PrologTerm;

public class ScopeCall implements Node {

	private final LtlParser parser;

	private Scope_callContext context;
	private Token token;
	private ScopeTypes type;
	private List<Variable> arguments = new LinkedList<Variable>();
	private List<VariableValue> argumentNodes = new LinkedList<VariableValue>();

	public ScopeCall(LtlParser parser, Scope_callContext context) {
		this.parser = parser;
		this.context = context;

		if (this.context != null) {
			determineTokenAndType();
			determineArguments();

			// Check types of arguments and parameters
			checkArguments();
		}
	}

	private void determineTokenAndType() {
		TerminalNode node = null;
		type = null;
		if (context.BEFORE_SCOPE() != null) {
			node = context.BEFORE_SCOPE();
			type = ScopeTypes.BEFORE;
		} else if (context.AFTER_SCOPE() != null) {
			node = context.AFTER_SCOPE();
			type = ScopeTypes.AFTER;
		} else if (context.BETWEEN_SCOPE() != null) {
			node = context.BETWEEN_SCOPE();
			type = ScopeTypes.BETWEEN;
		} else {
			node = context.UNTIL_SCOPE();
			type = ScopeTypes.AFTER_UNTIL;
		}

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
			Variable argument = arguments.get(i);
			if (!argument.getType().equals(VariableTypes.var)) {
				String msg = String.format("Type mismatch. Passed argument has the type '%s'. Expected type '%s'.", argument.getType(), VariableTypes.var);
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

	// Getters
	public Token getToken() {
		return token;
	}

	public ScopeTypes getType() {
		return type;
	}

	public List<Variable> getArguments() {
		return arguments;
	}

	public List<VariableValue> getArgumentNodes() {
		return argumentNodes;
	}

	public PrologTerm getValue(int index) {
		PrologTerm value = null;

		if (index < arguments.size()) {
			value = arguments.get(index).getValue();
		}

		return value;
	}

	@Override
	public String toString() {
		return String.format("scope(%s)", type.name());
	}

}
