package de.prob.ltl.parser.pattern;

import java.util.LinkedList;
import java.util.List;

import de.prob.ltl.parser.semantic.PatternDefinition;
import de.prob.ltl.parser.symboltable.SymbolTableManager;

public class PatternManager implements PatternUpdateListener {

	private List<Pattern> patterns = new LinkedList<Pattern>();
	private List<PatternUpdateListener> updateListeners = new LinkedList<PatternUpdateListener>();

	public void addPattern(Pattern pattern) {
		patterns.add(pattern);
		pattern.updateDefinitions(this);
		pattern.addUpdateListener(this);
		patternUpdated(pattern, null);
	}

	public void removePattern(Pattern pattern) {
		patterns.remove(pattern);
		pattern.removeUpdateListener(this);
		patternUpdated(pattern, null);
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

	public void addUpdateListener(PatternUpdateListener listener) {
		updateListeners.add(listener);
	}

	public void removeUpdateListener(PatternUpdateListener listener) {
		updateListeners.remove(listener);
	}

	public void removeUpdateListeners() {
		updateListeners.clear();
	}

	@Override
	public void patternUpdated(Pattern pattern, PatternManager patternManager) {
		pattern.updateDefinitions(this);
		for (PatternUpdateListener listener: updateListeners) {
			listener.patternUpdated(pattern, this);
		}
	}

}
