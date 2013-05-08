package de.prob.ltl.parser.symboltable;


import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import de.prob.ltl.parser.LtlParser;


public class SymbolTable {

	private LtlParser parser;
	private ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();
	private Scope globalScope = new Scope(null, null);
	private Scope currentScope = globalScope;

	public SymbolTable(LtlParser parser) {
		this.parser = parser;
	}

	public void define(Symbol symbol) {
		String symbolId = symbol.getSymbolID();
		Symbol otherSymbol = currentScope.resolve(symbolId);

		if (otherSymbol != null) {
			if (otherSymbol instanceof PatternSymbol) { // Pattern redefinition
				parser.notifyWarningListeners(symbolId + " is already defined.", symbol);
			} else if (currentScope.resolveLocal(symbolId) != null) { // For variables: only in same scope
				String msg = symbolId + " is already defined.";
				Token token = symbol.getToken();
				parser.notifyErrorListeners(token, msg, new RecognitionException(msg, parser, token.getInputStream(), null));
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

	public boolean scopeExists(ParseTree ctx) {
		return scopes.get(ctx) != null;
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

	public LtlParser getParser() {
		return parser;
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
