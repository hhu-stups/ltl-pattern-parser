package de.prob.ltl.parser.symbol;


public class PatternSymbol extends Symbol {

	private int args;

	public PatternSymbol(String name, int args) {
		super(name);
		this.args = args;
	}

	public int getArgs() {
		return args;
	}

	@Override
	public String toString() {
		return "Pattern " + name + "/" + args + "";
	}

}

