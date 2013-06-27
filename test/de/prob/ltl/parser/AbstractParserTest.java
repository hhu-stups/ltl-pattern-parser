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

import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.parserbase.ProBParserBase;
import de.prob.parserbase.UnparsedParserBase;
import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.output.PrologTermStringOutput;
import de.prob.prolog.term.PrologTerm;

public abstract class AbstractParserTest {

	enum ExceptionCause {
		DownwardIncompatible,
		Deprecated,
		Unsupported
	};

	interface ParserRuleCall {
		ParseTree callParserRule(LtlParser parser);

		SymbolTable getSymbolTable();
	}

	protected static ParserRuleCall DEFAULT_PARSER_RULE_CALL = new ParserRuleCall() {

		private SymbolTable symbolTable;

		@Override
		public ParseTree callParserRule(LtlParser parser) {
			ParseTree result = parser.start();

			ParseTreeWalker walker = new ParseTreeWalker();
			symbolTable = new SymbolTable();
			walker.walk(new SematicCheckPhase1(symbolTable), result);
			walker.walk(new SematicCheckPhase2(symbolTable), result);

			return result;
		}

		@Override
		public SymbolTable getSymbolTable() {
			return symbolTable;
		}
	};

	protected static UnparsedParserBase parserBase;
	protected static de.be4.ltl.core.parser.LtlParser oldParser;

	protected static ParserRuleCall parserRuleCall = DEFAULT_PARSER_RULE_CALL;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		parserBase = new UnparsedParserBase("expression", "predicate", "transition_predicate");
		oldParser = new de.be4.ltl.core.parser.LtlParser(parserBase);

		parserRuleCall = DEFAULT_PARSER_RULE_CALL;
	}

	protected ParseTree parseToTree(String input) {
		LtlLexer lexer = new LtlLexer(new ANTLRInputStream(input));
		LtlParser parser = new LtlParser(new CommonTokenStream(lexer));
		TestErrorListener errorListener = new TestErrorListener();
		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		lexer.addErrorListener(errorListener);
		parser.addErrorListener(errorListener);

		ParseTree result = parserRuleCall.callParserRule(parser);

		if (errorListener.getErrors() > 0) {
			throw errorListener.getExceptions().get(0);
		}

		return result;
	}

	protected LtlPrologTermGenerator createPrologGenerator(final IPrologTermOutput pto, String currentStateID, final ProBParserBase specParser) {
		return new LtlPrologTermGenerator(pto, "current", parserBase);
	}

	protected String parse(String input) {
		PrologTermStringOutput pto = new PrologTermStringOutput();
		LtlPrologTermGenerator generator = createPrologGenerator(pto, "current", parserBase);

		ParseTree ast = parseToTree(input);
		generator.generatePrologTerm(ast);
		return pto.toString();
	}

	protected String parseOld(String input) throws Exception {
		PrologTerm term = oldParser.generatePrologTerm(input, "current");
		return term.toString();
	}

	protected void assertEquals(String expected, String ... inputs) throws Exception {
		for (String input : inputs) {
			Assert.assertEquals(expected, parse(input));
			Assert.assertEquals(expected, parseOld(input));
		}
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

	protected void throwsException(String expected, String input, ExceptionCause cause) {
		if (cause == ExceptionCause.Deprecated || cause == ExceptionCause.Unsupported) {
			throwsException(input, "Exception for new parser version should have been thrown. (Input: "+input+")");
		} else {
			try {
				parse(input);
			} catch(Exception e) {
				Assert.fail("Exception for new parser version should not have been thrown. (Input: "+input+")");
			}
		}

		try {
			Assert.assertEquals(expected, parseOld(input));
			if (!cause.equals(ExceptionCause.Deprecated)) {
				Assert.fail("Exception for old parser version should have been thrown. (Input: "+input+")");
			}
		} catch(Exception ex) {
			if (cause.equals(ExceptionCause.Deprecated)) {
				Assert.fail("Exception for old parser version should not have been thrown. (Input: "+input+")");
			}
		}
		if (cause.equals(ExceptionCause.Deprecated)) {
			System.out.println("Deprecated input: " + input);
		}
	}

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
