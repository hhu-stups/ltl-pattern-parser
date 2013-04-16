package de.prob.ltl.parser.symbol;


public class VariableSymbol extends Symbol {

	public VariableSymbol(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Var " + name + "";
	}

}
