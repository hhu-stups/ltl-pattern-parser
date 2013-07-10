package de.prob.ltl.parser.symboltable;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class SymbolTable {

	private final Scope globalScope = new Scope(null);
	private Stack<Scope> scopeStack = new Stack<Scope>();
	private ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();

	public SymbolTable() {
		scopeStack.push(globalScope);
	}

	public void define(Symbol symbol) {
		String name = symbol.getSymbolID();
		if (isDefined(symbol)) {
			boolean isPattern = symbol instanceof Pattern;
			throw new RuntimeException(String.format("%s '%s' is already defined.", (isPattern ? "Pattern" : "Variable"), name));
		}
		getCurrentScope().define(symbol);
	}

	public boolean isDefined(Symbol symbol) {
		return isDefined(symbol.getSymbolID());
	}

	public boolean isDefined(String name) {
		return resolve(name) != null;
	}

	public Symbol resolve(String name) {
		return getCurrentScope().resolve(name);
	}

	public List<Symbol> getSymbols() {
		return getCurrentScope().getSymbols();
	}

	public Scope getCurrentScope() {
		return scopeStack.peek();
	}

	public void pushScope(Scope scope, ParserRuleContext context) {
		scopes.put(context, scope);
		scope.setDefinitionContext(context);
		pushScope(scope);
	}

	public void pushScope(Scope scope) {
		if (!getCurrentScope().equals(scope)) {
			scopeStack.push(scope);
		}
	}

	public void pushScope(ParserRuleContext context) {
		Scope scope = scopes.get(context);
		pushScope(scope);
	}

	public void popScope() {
		if (scopeStack.size() > 1) {
			scopeStack.pop();
		}
	}

	public boolean checkTypes(Pattern call) {
		Pattern definedPattern = (Pattern) resolve(call.getSymbolID());
		return definedPattern.checkParameterTypes(call.getParameters());
	}

}
