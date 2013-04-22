package de.prob.ltl.parser.symboltable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Scope {

	private Scope parent = null;
	private List<Scope> children = new LinkedList<Scope>();
	private Map<String, Symbol> symbols = new HashMap<String, Symbol>();

	public Scope(Scope parent) {
		this.parent = parent;
		if (this.parent != null) {
			this.parent.addChildScope(this);
		}
	}

	public void define(Symbol symbol) {
		String symbolId = symbol.getSymbolID();
		if (symbols.containsKey(symbolId)) {
			// TODO: return warning (redefinition)
		}
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

	public void addChildScope(Scope scope) {
		children.add(scope);
	}

	public boolean hasParent() {
		return parent != null;
	}

	public Scope getParent() {
		return parent;
	}

	public List<Scope> getChildren() {
		return children;
	}

	public Collection<Symbol> getSymbols() {
		return symbols.values();
	}

}