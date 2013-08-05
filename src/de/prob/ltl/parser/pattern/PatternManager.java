package de.prob.ltl.parser.pattern;

import java.util.LinkedList;
import java.util.List;

import de.prob.ltl.parser.semantic.PatternDefinition;
import de.prob.ltl.parser.symboltable.SymbolTableManager;

public class PatternManager  {

	private List<Pattern> patterns = new LinkedList<Pattern>();

	public void addPattern(Pattern pattern) {
		patterns.add(pattern);
		pattern.updateDefinitions(this);
	}

	public void removePattern(Pattern pattern) {
		patterns.remove(pattern);
	}

	public void updatePatterns(SymbolTableManager symbolTableManager) {
		for (Pattern pattern : patterns) {
			if (pattern.getDefinitions() != null) {
				for (PatternDefinition definition : pattern.getDefinitions()) {
					symbolTableManager.define(definition);
				}
			}
		}
	}

}
