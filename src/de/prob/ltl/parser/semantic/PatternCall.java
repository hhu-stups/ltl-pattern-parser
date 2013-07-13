package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.AfterScopeCallContext;
import de.prob.ltl.parser.LtlParser.AfterUntilScopeCallContext;
import de.prob.ltl.parser.LtlParser.BeforeScopeCallContext;
import de.prob.ltl.parser.LtlParser.BetweenScopeCallContext;
import de.prob.ltl.parser.LtlParser.Pattern_callContext;
import de.prob.ltl.parser.LtlParser.Pattern_call_scopeContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.symboltable.PatternScopes;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.output.StructuredPrologOutput;

public class PatternCall implements Node {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private Pattern_callContext context;
	private Token token;
	private String name;
	private PatternScopes scope;
	private List<Variable> scopeArguments = new LinkedList<Variable>();
	private List<Variable> arguments = new LinkedList<Variable>();
	private List<Node> scopeArgumentNodes = new LinkedList<Node>();
	private List<Node> argumentNodes = new LinkedList<Node>();
	private PatternDefinition definition;

	public PatternCall(LtlParser parser, Pattern_callContext context) {
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			determineTokenAndName();
			determineScopeAndScopeArguments();
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

	private void determineScopeAndScopeArguments() {
		scope = PatternScopes.global;

		Pattern_call_scopeContext ctx = context.pattern_call_scope();
		if (ctx != null) {
			if (ctx instanceof BeforeScopeCallContext) {
				scope = PatternScopes.before;
				ExprOrAtom argumentAtom = new ExprOrAtom(parser, ((BeforeScopeCallContext) ctx).atom());
				addArgument(scopeArguments, scopeArgumentNodes, new Variable(null, VariableTypes.var), argumentAtom);
			} else if (ctx instanceof AfterScopeCallContext) {
				scope = PatternScopes.after;
				ExprOrAtom argumentAtom = new ExprOrAtom(parser, ((AfterScopeCallContext) ctx).atom());
				addArgument(scopeArguments, scopeArgumentNodes, new Variable(null, VariableTypes.var), argumentAtom);
			} else if (ctx instanceof BetweenScopeCallContext) {
				scope = PatternScopes.between;
				for (int i = 0; i < ((BetweenScopeCallContext) ctx).atom().size(); i++) {
					ExprOrAtom argumentAtom = new ExprOrAtom(parser, ((BetweenScopeCallContext) ctx).atom(i));
					addArgument(scopeArguments, scopeArgumentNodes, new Variable(null, VariableTypes.var), argumentAtom);
				}
			} else if (ctx instanceof AfterUntilScopeCallContext) {
				scope = PatternScopes.after_until;
				for (int i = 0; i < ((AfterUntilScopeCallContext) ctx).atom().size(); i++) {
					ExprOrAtom argumentAtom = new ExprOrAtom(parser, ((AfterUntilScopeCallContext) ctx).atom(i));
					addArgument(scopeArguments, scopeArgumentNodes, new Variable(null, VariableTypes.var), argumentAtom);
				}
			}
		}
	}

	private void determineArguments() {
		for (Var_valueContext ctx : context.var_value()) {
			VariableValue value = new VariableValue(parser, ctx);

			Variable argument = new Variable(null, value.getValueType());
			argument.setToken(value.getToken());
			addArgument(arguments, argumentNodes, argument, value);
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

	private void addArgument(List<Variable> argumentList, List<Node> valueList, Variable var, Node node) {
		argumentList.add(var);
		valueList.add(node);
	}

	public String getName() {
		return String.format("%s/%s/%d", name, scope, arguments.size());
	}

	// Getters
	public Token getToken() {
		return token;
	}

	public String getSimpleName() {
		return name;
	}

	public PatternScopes getScope() {
		return scope;
	}

	public List<Variable> getScopeArguments() {
		return scopeArguments;
	}

	public List<Variable> getArguments() {
		return arguments;
	}

	@Override
	public String toString() {
		return String.format("call(%s)", getName());
	}

	@Override
	public void createPrologTerm(LtlParser parser, IPrologTermOutput pto,
			String currentState, ProBParserBase parserBase) {
		for (int i = 0; i < scopeArgumentNodes.size(); i++) {
			Variable parameter = definition.getScopeParameters().get(i);
			Node argument = scopeArgumentNodes.get(i);
			StructuredPrologOutput temp = new StructuredPrologOutput();
			argument.createPrologTerm(parser, temp, currentState, parserBase);
			temp.fullstop();
			parameter.setValue(temp.getSentences().get(0));
		}
		for (int i = 0; i < argumentNodes.size(); i++) {
			Variable parameter = definition.getParameters().get(i);
			Node argument = argumentNodes.get(i);
			StructuredPrologOutput temp = new StructuredPrologOutput();
			argument.createPrologTerm(parser, temp, currentState, parserBase);
			temp.fullstop();
			parameter.setValue(temp.getSentences().get(0));
		}

		symbolTableManager.pushScope(definition);
		for (Node node : symbolTableManager.getNodes()) {
			node.createPrologTerm(parser, pto, currentState, parserBase);
		}
		symbolTableManager.popScope();
	}

}
