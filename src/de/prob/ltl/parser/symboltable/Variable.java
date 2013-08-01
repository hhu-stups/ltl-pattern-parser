package de.prob.ltl.parser.symboltable;

import org.antlr.v4.runtime.Token;


public class Variable {

	private String name;
	private VariableTypes type;

	private Token token;

	public Variable(String name, VariableTypes type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public VariableTypes getType() {
		return type;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, type);
	}

}
