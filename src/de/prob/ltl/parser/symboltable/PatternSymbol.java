package de.prob.ltl.parser.symboltable;

import org.antlr.v4.runtime.Token;


public class PatternSymbol extends Symbol {

	private int args;

	public PatternSymbol(String name, Token token, int args) {
		super(name, token);
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

