package de.prob.ltl.parser.symboltable;

import org.antlr.v4.runtime.Token;


public class PatternSymbol extends Symbol {

	private PatternScope patternScope;
	private int args;

	public PatternSymbol(String name, Token token, PatternScope patternScope, int args) {
		super(name, token);
		this.patternScope = patternScope;
		this.args = args;
	}

	public PatternSymbol(String name, Token token, int args) {
		this(name, token, PatternScope.global, args);
	}

	public PatternScope getPatternScope() {
		return patternScope;
	}

	public int getArgs() {
		return args;
	}

	@Override
	public String getSymbolID() {
		return name + "/" + patternScope.name() + "/" + args;
	}

}

