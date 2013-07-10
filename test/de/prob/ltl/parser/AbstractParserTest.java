package de.prob.ltl.parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.BeforeClass;

import de.prob.ltl.parser.prolog.PrologTermGenerator;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.parserbase.UnparsedParserBase;
import de.prob.prolog.output.StructuredPrologOutput;

public abstract class AbstractParserTest {

	protected static UnparsedParserBase parserBase;

	@BeforeClass
	public static void setUpBeforeClass() {
		parserBase = new UnparsedParserBase("expression", "predicate", "transition_predicate");
	}

	protected LtlParser createParser(String input) {
		LtlLexer lexer = new LtlLexer(new ANTLRInputStream(input));
		LtlParser parser = new LtlParser(new CommonTokenStream(lexer));

		TestErrorListener errorListener = new TestErrorListener();
		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		lexer.addErrorListener(errorListener);
		parser.addErrorListener(errorListener);

		return parser;
	}

	protected void semanticCheck(ParseTree ast, SymbolTable symbolTable) {
		ParseTreeWalker.DEFAULT.walk(new SematicCheckPhase1(symbolTable), ast);
		ParseTreeWalker.DEFAULT.walk(new SematicCheckPhase2(symbolTable), ast);
	}

	protected boolean hasErrors(LtlParser parser) {
		if (parser.getErrorListeners().size() > 0 && parser.getErrorListeners().get(0) instanceof TestErrorListener) {
			TestErrorListener listener = (TestErrorListener) parser.getErrorListeners().get(0);

			if (listener.getErrors() > 0) {
				return true;
			}
		}
		return false;
	}

	protected StructuredPrologOutput generatePrologTerm(ParseTree ast, SymbolTable symbolTable) {
		StructuredPrologOutput pto = new StructuredPrologOutput();
		PrologTermGenerator generator = new PrologTermGenerator(symbolTable, pto, "current", parserBase);

		generator.generatePrologTerm(ast);
		pto.fullstop();
		return pto;
	}

	protected void parse(String input) {
		LtlParser parser = createParser(input);

		ParseTree ast = parser.start();

		semanticCheck(ast, new SymbolTable());
		if (hasErrors(parser)) {
			throw new RuntimeException("Exception was thrown during parsing.");
		}
	}

	protected String parseToString(String input) {
		LtlParser parser = createParser(input);

		ParseTree ast = parser.start();

		SymbolTable symbolTable = new SymbolTable();
		semanticCheck(ast, symbolTable);
		if (hasErrors(parser)) {
			throw new RuntimeException("Exception was thrown during parsing.");
		}

		return generatePrologTerm(ast, symbolTable).getSentences().get(0).toString();
	}

	protected void throwsException(String input, String msg) {
		try {
			parse(input);
			Assert.fail(msg);
		} catch(RuntimeException e) {
		}
	}

	protected void throwsException(String input) {
		throwsException(input, "Exception should have been thrown. (Input: \""+ input +"\")");
	}

	// Test helper class
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

}
