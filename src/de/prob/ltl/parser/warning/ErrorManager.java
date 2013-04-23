package de.prob.ltl.parser.warning;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import de.prob.ltl.parser.symboltable.Symbol;

public class ErrorManager {

	private List<WarningMessage> warnings = new LinkedList<WarningMessage>();

	public void addWarning(String message, Symbol ... symbols) {
		warnings.add(new WarningMessage(message, symbols));
	}

	public List<WarningMessage> getWarnings() {
		return warnings;
	}

	public void throwError(Token token, String message) {
		String output = String.format("line %d:%d %s\n", token.getLine(), token.getCharPositionInLine(), message);
		throw new RuntimeException(output);
	}

}
