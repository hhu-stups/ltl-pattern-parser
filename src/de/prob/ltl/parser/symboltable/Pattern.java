package de.prob.ltl.parser.symboltable;

import java.util.LinkedList;
import java.util.List;

public class Pattern extends Scope implements Symbol {

	public enum PatternScopes {
		global,
		after,
		before,
		between,
		after_until
	}

	private String name;
	private PatternScopes scope = PatternScopes.global;
	private List<Variable> parameters = new LinkedList<Variable>();
	private List<Variable> scopeParameters = new LinkedList<Variable>();

	public Pattern(Scope parent, String name) {
		super(parent);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public PatternScopes getScope() {
		return scope;
	}

	public void setScope(PatternScopes scope) {
		this.scope = scope;
	}

	public List<Variable> getParameters() {
		return parameters;
	}

	public void addParameter(Variable parameter) {
		parameters.add(parameter);
	}

	public List<Variable> getScopeParameters() {
		return scopeParameters;
	}

	public void addScopeParameter(Variable parameter) {
		scopeParameters.add(parameter);
	}

	public boolean checkParameterTypes(List<Variable> otherParameters) {
		for (int i = 0; i < parameters.size(); i++) {
			if (!otherParameters.get(i).getType().equals(parameters.get(i).getType())) {
				return false;
			}
		}
		return true;
	}

	public int getParameterIndex(Variable var) {
		return parameters.indexOf(var);
	}

	@Override
	public void define(Symbol symbol) {
		if (symbol instanceof Pattern) {
			throw new RuntimeException("You cannot define nested patterns.");
		}
		super.define(symbol);
	}

	@Override
	public Symbol resolve(String name) {
		Symbol symbol = resolveLocal(name);

		if (symbol == null) {
			symbol = super.resolve(name);
			if (symbol != null && !(symbol instanceof Pattern)) {
				symbol = null;
			}
		}

		return symbol;
	}

	@Override
	public List<Symbol> getSymbols() {
		List<Symbol> result = getLocalSymbols();
		if (hasParent()) {
			for (Symbol symbol : parent.getSymbols()) {
				if (symbol instanceof Pattern) {
					result.add(symbol);
				}
			}
		}
		return result;
	}

	@Override
	public String getSymbolID() {
		int params = parameters.size();
		return getSymbolID(name, scope, params);
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", getSymbolID(), parameters);
	}

	public static String getSymbolID(String name, PatternScopes scope, int params) {
		return String.format("%s/%s/%s", name, scope.name(), params);
	}

}
