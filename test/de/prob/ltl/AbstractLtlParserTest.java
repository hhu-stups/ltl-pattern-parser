package de.prob.ltl;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.LexerNoViableAltException;
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
	public void throwsRuntimeException(String input) {
		try {
			parse(input);
			Assert.fail("RuntimeException should have been thrown. (Input: \""+ input +"\")");
		} catch(RuntimeException e) {
		}
	}

	protected String parse(String input, WarningListener listener) {
		final LtlParser parser = ParserFactory.createLtlParser(createLexer(input), new BailErrorStrategy());
		if (listener != null) {
			parser.addWarningListener(listener);
		}
		parser.removeErrorListeners();
		parser.addErrorListener(new BaseErrorListener() {

			@Override
			public void syntaxError(Recognizer<?, ?> recognizer,
					Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				throw e;
			}

		});

		ParseTree result = parser.start();
		parser.semanticCheck(result);

		StringRepresentationGenerator generator = new StringRepresentationGenerator(getProBParserBase());
		generator.visit(result);
		return generator.getGeneratedString();
	}

	protected String parse(String input) {
		return parse(input, null);
	}

	protected LtlLexer createLexer(String input) {
		ANTLRInputStream inputStream = new ANTLRInputStream(input);
		LtlLexer lexer = new LtlLexer(inputStream) {
			@Override
			public void recover(LexerNoViableAltException e) {
				throw new RuntimeException(e); // Bail out
			}

			@Override
			public void recover(RecognitionException re) {
				throw new RuntimeException(re); // Bail out
			}
		};
		lexer.removeErrorListeners();
		return lexer;
	}

}
