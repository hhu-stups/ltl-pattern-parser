package de.prob.ltl.parser.warning;

import de.prob.ltl.parser.symboltable.Symbol;

public class WarningMessage {

	private Symbol symbols[];
	private String message;

	public WarningMessage(String message, Symbol ... symbols) {
		this.message = message;
		this.symbols = symbols;
	}

	public Symbol[] getSymbols() {
		return symbols;
	}

	public String getMessage() {
		return message;
	}

}
