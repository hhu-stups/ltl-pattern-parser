package de.prob.ltl.parser.symboltable;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class SymbolTable {

	private final Scope globalScope = new Scope(null);
	private Scope currentScope = globalScope;
	private ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();

	public void define(Symbol symbol) {
		String name = symbol.getSymbolID();
		if (isDefined(symbol)) {
			boolean isPattern = symbol instanceof Pattern;
			throw new RuntimeException(String.format("%s '%s' is already defined.", (isPattern ? "Pattern" : "Variable"), name));
		}
		currentScope.define(symbol);
	}

	public boolean isDefined(Symbol symbol) {
		return isDefined(symbol.getSymbolID());
	}

	public boolean isDefined(String name) {
		return resolve(name) != null;
	}

	public Symbol resolve(String name) {
		return currentScope.resolve(name);
	}

	public List<Symbol> getSymbols() {
		return currentScope.getSymbols();
	}

	public Scope getCurrentScope() {
		return currentScope;
	}

	public void pushScope(Scope scope, ParserRuleContext context) {
		currentScope = scope;
		scopes.put(context, scope);
	}

	public void pushScope(ParserRuleContext context) {
		currentScope = scopes.get(context);
	}

	public void popScope() {
		if (currentScope.hasParent()) {
			currentScope = currentScope.getParent();
		} else {
			currentScope = globalScope;
		}
	}

	public boolean checkTypes(Pattern call) {
		Pattern definedPattern = (Pattern) resolve(call.getSymbolID());
		return definedPattern.checkParameterTypes(call.getParameters());
	}

}
