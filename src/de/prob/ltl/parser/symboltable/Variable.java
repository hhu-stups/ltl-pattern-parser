package de.prob.ltl.parser.symboltable;

import org.antlr.v4.runtime.Token;

import de.prob.prolog.term.PrologTerm;


public class Variable implements Symbol {

	public enum VariableTypes {
		var,
		num
	}

	private String name;
	private VariableTypes type;
	private PrologTerm value;
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

	public void setType(VariableTypes type) {
		this.type = type;
	}

	@Override
	public String getSymbolID() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, (type != null ? type.name() : null));
	}

	public PrologTerm getValue() {
		return value;
	}

	public void setValue(PrologTerm value) {
		this.value = value;
	}

	@Override
	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

}
