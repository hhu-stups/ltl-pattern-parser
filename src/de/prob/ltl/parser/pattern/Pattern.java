package de.prob.ltl.parser.pattern;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.WarningListener;
import de.prob.ltl.parser.semantic.PatternDefinition;


public class Pattern {

	private String description;
	private String code;
	private List<PatternDefinition> definitions;
	private List<BaseErrorListener> errorListeners = new LinkedList<BaseErrorListener>();
	private List<WarningListener> warningListeners = new LinkedList<WarningListener>();

	public void updateDefinitions(PatternManager patternManager) {
		if (code != null) {
			if (definitions == null) {
				LtlParser parser = new LtlParser(code);
				parser.setPatternManager(patternManager);
				parser.removeErrorListeners();
				for (BaseErrorListener listener : errorListeners) {
					parser.addErrorListener(listener);
				}
				for (WarningListener listener : warningListeners) {
					parser.addWarningListener(listener);
				}

				parser.parsePatternDefinition();

				definitions = parser.getSymbolTableManager().getPatternDefinitions();
			}
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		definitions = null;
	}

	public List<PatternDefinition> getDefinitions() {
		return definitions;
	}

	public void addErrorListener(BaseErrorListener listener) {
		errorListeners.add(listener);
	}

	public void removeErrorListener(BaseErrorListener listener) {
		errorListeners.remove(listener);
	}

	public void removeErrorListeners() {
		errorListeners.clear();
	}

	public void addWarningListener(WarningListener listener) {
		warningListeners.add(listener);
	}

	public void removeWarningListener(WarningListener listener) {
		warningListeners.remove(listener);
	}

	public void removeWarningListeners() {
		warningListeners.clear();
	}

}
