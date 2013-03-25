package de.prob.ltl;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;

import de.prob.ltl.parser.LtlLexer;
import de.prob.ltl.parser.LtlParser;
import de.prob.parserbase.ProBParserBase;


public abstract class AbstractLtlParserTest {

	public abstract ProBParserBase getProBParserBase();

	// Helper
	protected String parse(String input) {
		return parse(input, false);
	}

	protected String parse(String input, boolean hideErrors) {
		final LtlParser parser = createParser(input);
		if (hideErrors) {
			parser.removeErrorListeners();
		}
		ParseTree result = parser.start();
		StringRepresentationGenerator generator = new StringRepresentationGenerator(getProBParserBase());
		generator.visit(result);
		return generator.getGeneratedString();
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

	protected LtlParser createParser(LtlLexer lexer) {
		LtlParser parser = new LtlParser(new CommonTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		return parser;
	}

	protected LtlParser createParser(String input) {
		return createParser(createLexer(input));
	}

}
