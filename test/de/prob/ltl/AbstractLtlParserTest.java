package de.prob.ltl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;

import de.prob.ltl.parser.LtlLexer;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.ParserFactory;
import de.prob.ltl.parser.warning.WarningListener;
import de.prob.parserbase.ProBParserBase;


public abstract class AbstractLtlParserTest {

	public abstract ProBParserBase getProBParserBase();

	// Helper
	protected void throwsException(String input, String msg) {
		TestErrorListener listener = new TestErrorListener();
		LtlLexer lexer = ParserFactory.createLtlLexer(input, listener);
		LtlParser parser = ParserFactory.createLtlParser(lexer, listener);

		ParseTree result = parser.start();
		parser.semanticCheck(result);

		if (listener.getErrors() == 0) {
			Assert.fail(msg);
		}
	}

	protected void throwsException(String input) {
		throwsException(input, "Exception should have been thrown. (Input: \""+ input +"\")");
	}

	protected String parse(String input, WarningListener listener) {
		LtlLexer lexer = ParserFactory.createLtlLexer(input);
		LtlParser parser = ParserFactory.createLtlParser(lexer);
		BaseErrorListener errorListener = new BaseErrorListener() {

			@Override
			public void syntaxError(Recognizer<?, ?> recognizer,
					Object offendingSymbol, int line,
					int charPositionInLine, String msg,
					RecognitionException e) {
				if (e == null) {
					throw new RecognitionException(msg, recognizer, null, null);
				}
				throw e;
			}

		};
		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		lexer.addErrorListener(errorListener);
		parser.addErrorListener(errorListener);

		if (listener != null) {
			parser.addWarningListener(listener);
		}

		ParseTree result = parser.start();
		parser.semanticCheck(result);

		StringRepresentationGenerator generator = new StringRepresentationGenerator(getProBParserBase());
		generator.visit(result);
		return generator.getGeneratedString();
	}

	protected String parse(String input) {
		return parse(input, null);
	}

	protected class TestErrorListener extends BaseErrorListener {

		private int errors = 0;

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
				Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			errors++;
		}

		public int getErrors() {
			return errors;
		}

	}

}
