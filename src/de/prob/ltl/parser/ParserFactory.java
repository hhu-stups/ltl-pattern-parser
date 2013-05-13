package de.prob.ltl.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;

public class ParserFactory {

	public static LtlParser createLtlParser(LtlLexer lexer) {
		return new LtlParser(new CommonTokenStream(lexer));
	}

	public static LtlParser createLtlParser(String input) {
		return createLtlParser(createLtlLexer(input));
	}

	public static LtlParser createLtlParser(LtlLexer lexer, BaseErrorListener ... errorListeners) {
		LtlParser parser = createLtlParser(lexer);
		parser.removeErrorListeners();
		for (BaseErrorListener listener : errorListeners) {
			parser.addErrorListener(listener);
		}

		return parser;
	}

	public static LtlParser createLtlParser(String input, BaseErrorListener ... errorListeners) {
		return createLtlParser(createLtlLexer(input), errorListeners);
	}

	public static LtlLexer createLtlLexer(String input) {
		return new LtlLexer(new ANTLRInputStream(input));
	}

	public static LtlLexer createLtlLexer(String input, BaseErrorListener ... errorListeners) {
		LtlLexer lexer = createLtlLexer(input);
		lexer.removeErrorListeners();
		for (BaseErrorListener listener : errorListeners) {
			lexer.addErrorListener(listener);
		}

		return lexer;
	}

}
