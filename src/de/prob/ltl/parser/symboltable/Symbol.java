package de.prob.ltl.parser.symboltable;

import org.antlr.v4.runtime.Token;

public class Symbol {

	protected String name;
	protected Token token;

	public Symbol(String name, Token token) {
		this.name = name;
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public Token getToken() {
		return token;
	}

	public String getSymbolID() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getSymbolID() == null) ? 0 : getSymbolID().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Symbol other = (Symbol) obj;
		if (getSymbolID() == null) {
			if (other.getSymbolID() != null) {
				return false;
			}
		} else if (!getSymbolID().equals(other.getSymbolID())) {
			return false;
		}
		return true;
	}

}
