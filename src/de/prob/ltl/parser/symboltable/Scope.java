package de.prob.ltl.parser.symboltable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Scope {

	private Scope parent = null;
	private Symbol scopeSymbol = null;
	private List<Scope> children = new LinkedList<Scope>();
	private Map<String, Symbol> symbols = new HashMap<String, Symbol>();

	public Scope(Scope parent, Symbol scopeSymbol) {
		this.parent = parent;
		this.scopeSymbol = scopeSymbol;
		if (this.parent != null) {
			this.parent.addChildScope(this);
		}
	}

	public void define(Symbol symbol) {
		String symbolId = symbol.getSymbolID();
		symbols.put(symbolId, symbol);
	}

	public Symbol resolve(String name) {
		// Look for symbol in this scope
		Symbol symbol = symbols.get(name);

		// Search in parent scope(s)
		if (symbol == null && parent != null) {
			return parent.resolve(name);
		}
		return symbol;
	}

	public Symbol resolveLocal(String name) {
		// Look for symbol in this scope
		return symbols.get(name);
	}

	public void addChildScope(Scope scope) {
		children.add(scope);
	}

	public boolean hasParent() {
		return parent != null;
	}

	public Scope getParent() {
		return parent;
	}

	public Symbol getScopeSymbol() {
		return scopeSymbol;
	}

	public List<Scope> getChildren() {
		return children;
	}

	public List<Symbol> getSymbols() {
		return new LinkedList<Symbol>(symbols.values());
	}

}