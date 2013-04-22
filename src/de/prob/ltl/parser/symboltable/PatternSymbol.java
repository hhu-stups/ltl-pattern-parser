package de.prob.ltl.parser.symboltable;


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
	public String getSymbolID() {
		return name + "/" + args;
	}

}

