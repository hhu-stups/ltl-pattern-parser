package de.prob.ltl.parser.symboltable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Scope {

	protected Scope parent = null;
	protected Map<String, Symbol> symbols = new HashMap<String, Symbol>();

	public Scope(Scope parent) {
		this.parent = parent;
	}

	public void define(Symbol symbol) {
		String symbolId = symbol.getSymbolID();
		symbols.put(symbolId, symbol);
	}

	public Symbol resolve(String name) {
		// Look for symbol in this scope
		Symbol symbol = resolveLocal(name);

		// Search in parent scope(s)
		if (symbol == null && hasParent()) {
			return parent.resolve(name);
		}
		return symbol;
	}

	public Symbol resolveLocal(String name) {
		// Look for symbol in this scope
		return symbols.get(name);
	}

	public boolean hasParent() {
		return parent != null;
	}

	public Scope getParent() {
		return parent;
	}

	public void setParent(Scope parent) {
		this.parent = parent;
	}

	public List<Symbol> getSymbols() {
		List<Symbol> result = getLocalSymbols();
		if (hasParent()) {
			result.addAll(parent.getSymbols());
		}
		return result;
	}

	public List<Symbol> getLocalSymbols() {
		return new LinkedList<Symbol>(symbols.values());
	}

}