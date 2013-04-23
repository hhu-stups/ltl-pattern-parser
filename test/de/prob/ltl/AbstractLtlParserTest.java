package de.prob.ltl;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;

import de.prob.ltl.parser.LtlLexer;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.symbolcheck.SymbolChecker;
import de.prob.ltl.parser.symbolcheck.SymbolCollector;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.warning.ErrorManager;
import de.prob.parserbase.ProBParserBase;


public abstract class AbstractLtlParserTest {

	private ErrorManager errorManager;

	public abstract ProBParserBase getProBParserBase();

	// Helper
	public void throwsRuntimeException(String input) {
		try {
			parse(input, true);
			Assert.fail("RuntimeException should have been thrown. (Input: \""+ input +"\")");
		} catch(RuntimeException e) {
		}
	}

	protected String parse(String input) {
		return parse(input, false);
	}

	protected String parse(String input, boolean hideErrors) {
		final LtlParser parser = createParser(input);
		if (hideErrors) {
			parser.removeErrorListeners();
		}
		ParseTree result = parser.start();

		// Check symbols
		errorManager = new ErrorManager();
		SymbolTable symbolTable = new SymbolTable(errorManager);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new SymbolCollector(symbolTable), result);
		walker.walk(new SymbolChecker(symbolTable), result);

		StringRepresentationGenerator generator = new StringRepresentationGenerator(getProBParserBase());
		generator.visit(result);
		return generator.getGeneratedString();
	}

	public ErrorManager getErrorManager() {
		return errorManager;
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
