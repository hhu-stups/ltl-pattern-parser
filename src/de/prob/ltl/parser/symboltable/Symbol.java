package de.prob.ltl.parser.symboltable;

import org.antlr.v4.runtime.Token;

public interface Symbol {

	String getSymbolID();
	Token getToken();

}
