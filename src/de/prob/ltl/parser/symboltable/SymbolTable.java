package de.prob.ltl.parser.symboltable;


import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import de.prob.ltl.parser.warning.ErrorManager;


public class SymbolTable {

	private ErrorManager errorManager;
	private ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();
	private Scope globalScope = new Scope(null, null);
	private Scope currentScope = globalScope;

	public SymbolTable(ErrorManager errorManager) {
		this.errorManager = errorManager;
	}

	public void define(Symbol symbol) {
		String symbolId = symbol.getSymbolID();
		Symbol otherSymbol = currentScope.resolve(symbolId);

		if (otherSymbol != null) {
			if (otherSymbol instanceof PatternSymbol) { // Pattern redefinition
				errorManager.addWarning(symbolId + " is already defined.", symbol);
			} else if (currentScope.resolveLocal(symbolId) != null) { // For variables: only in same scope
				errorManager.throwError(otherSymbol.getToken(), symbolId + " is already defined.");
			}
		}

		currentScope.define(symbol);
	}

	public Symbol resolve(String name) {
		return currentScope.resolve(name);
	}

	public void pushScope(ParseTree ctx, Symbol scopeSymbol) {
		Scope scope = scopes.get(ctx);
		if (scope == null) {
			scope = new Scope(currentScope, scopeSymbol);
			scopes.put(ctx, scope);
		}
		currentScope = scope;
	}

	public void popScope() {
		if (currentScope.hasParent()) {
			currentScope = currentScope.getParent();
		}
	}

	public void setCurrentScope(ParseTree ctx) {
		if (ctx == null) {
			currentScope = globalScope;
		} else {
			currentScope = scopes.get(ctx);
		}
	}

	public Scope getCurrentScope() {
		return currentScope;
	}

	public Scope getGlobalScope() {
		return globalScope;
	}

	public ErrorManager getErrorManager() {
		return errorManager;
	}

	public void print() {
		print(globalScope);
	}

	private void print(Scope scope) {
		System.out.println((scope.hasParent() ? "local" : "global") + " scope: " + scope.getSymbols());
		for (Scope child : scope.getChildren()) {
			print(child);
		}
	}

}
