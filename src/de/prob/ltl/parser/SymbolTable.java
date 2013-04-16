package de.prob.ltl.parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import de.prob.ltl.parser.symbol.PatternSymbol;
import de.prob.ltl.parser.symbol.Symbol;

public class SymbolTable {

	private ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();
	private Scope globalScope = new Scope(null);
	private Scope currentScope = globalScope;

	public void define(Symbol symbol) {
		currentScope.define(symbol);
	}

	public Symbol resolve(String name) {
		return currentScope.resolve(name);
	}

	public Symbol resolve(String name, int args) {
		return currentScope.resolve(name, args);
	}

	public void pushScope(ParseTree ctx) {
		Scope scope = new Scope(currentScope);
		currentScope.addChild(scope);
		currentScope = scope;
		scopes.put(ctx, scope);
	}

	public void popScope() {
		if (currentScope.hasParent()) {
			currentScope = currentScope.getParent();
		}
	}

	public void setCurrentScope(ParseTree ctx) {
		currentScope = scopes.get(ctx);
	}

	public Scope getCurrentScope() {
		return currentScope;
	}

	public Scope getGlobalScope() {
		return globalScope;
	}

	public void print() {
		print(globalScope);
	}

	private void print(Scope scope) {
		System.out.println((scope.hasParent() ? "local" : "global") + " scope: " + scope.symbols);
		for (Scope child : scope.children) {
			print(child);
		}
	}

	class Scope {

		private Scope parent = null;
		private List<Scope> children = new LinkedList<Scope>();
		private List<Symbol> symbols = new LinkedList<Symbol>();

		public Scope(Scope parent) {
			this.parent = parent;
		}

		public Symbol resolve(String name) {
			for (Symbol symbol : symbols) {
				if (symbol.getName().equals(name)) {
					return symbol;
				}
			}

			if (parent != null) {
				return parent.resolve(name);
			}
			return null;
		}

		public Symbol resolve(String name, int args) {
			Symbol symbol = resolve(name);
			if (symbol != null && symbol instanceof PatternSymbol) {
				if (((PatternSymbol) symbol).getArgs() == args) {
					return symbol;
				}
			}
			return null;
		}

		public void addChild(Scope scope) {
			children.add(scope);
		}

		public void define(Symbol symbol) {
			symbols.add(symbol);
		}

		public Scope getParent() {
			return parent;
		}

		public boolean hasParent() {
			return parent != null;
		}

	}

}
