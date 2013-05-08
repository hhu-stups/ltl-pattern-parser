package de.prob.ltl.parser.warning;

import de.prob.ltl.parser.symboltable.Symbol;

public interface WarningListener {

	public void warning(String message, Symbol ... symbols);

}
