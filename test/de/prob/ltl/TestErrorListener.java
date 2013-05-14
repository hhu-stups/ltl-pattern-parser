package de.prob.ltl;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class TestErrorListener extends BaseErrorListener {

	private List<RuntimeException> exceptions = new LinkedList<RuntimeException>();

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer,
			Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		if (e == null) {
			exceptions.add(new RuntimeException(msg));
		} else {
			exceptions.add(e);
		}
	}

	public int getErrors() {
		return exceptions.size();
	}

	public List<RuntimeException> getExceptions() {
		return exceptions;
	}

}