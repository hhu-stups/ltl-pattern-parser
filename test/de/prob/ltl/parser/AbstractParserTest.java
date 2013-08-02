package de.prob.ltl.parser;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Assert;
import org.junit.BeforeClass;

import de.prob.ltl.parser.LtlParser.StartContext;
import de.prob.ltl.parser.prolog.LtlPrologTermGenerator;
import de.prob.ltl.parser.semantic.SemanticCheck;
import de.prob.parserbase.UnparsedParserBase;
import de.prob.prolog.output.StructuredPrologOutput;

public abstract class AbstractParserTest {

	protected static UnparsedParserBase parserBase;

	@BeforeClass
	public static void setUpBeforeClass() {
		parserBase = new UnparsedParserBase("expression", "predicate", "transition_predicate");
	}

	protected LtlParser createParser(String input) {
		return createParser(input, new TestErrorListener());
	}

	protected LtlParser createParser(String input, TestErrorListener errorListener) {
		LtlLexer lexer = new LtlLexer(new ANTLRInputStream(input));
		LtlParser parser = new LtlParser(new CommonTokenStream(lexer));

		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		lexer.addErrorListener(errorListener);
		parser.addErrorListener(errorListener);

		return parser;
	}

	protected SemanticCheck semanticCheck(StartContext ast, LtlParser parser) {
		SemanticCheck sc = new SemanticCheck(parser);
		sc.check(ast.body());

		return sc;
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

	protected List<RuntimeException> getExceptions(LtlParser parser) {
		if (parser.getErrorListeners().size() > 0 && parser.getErrorListeners().get(0) instanceof TestErrorListener) {
			TestErrorListener listener = (TestErrorListener) parser.getErrorListeners().get(0);

			return listener.getExceptions();
		}
		return Collections.emptyList();
	}

	protected StructuredPrologOutput generatePrologTerm(SemanticCheck check, LtlParser parser) {
		StructuredPrologOutput pto = new StructuredPrologOutput();
		LtlPrologTermGenerator generator = new LtlPrologTermGenerator(parser, "current", parserBase);

		generator.generatePrologTerm(pto, check.getBody());
		pto.fullstop();
		return pto;
	}

	protected void parse(String input) {
		LtlParser parser = createParser(input);

		StartContext ast = parser.start();

		semanticCheck(ast, parser);
		if (hasErrors(parser)) {
			throw getExceptions(parser).get(0);
		}
	}

	protected String parseToString(String input) {
		LtlParser parser = createParser(input);

		StartContext ast = parser.start();

		SemanticCheck sc = semanticCheck(ast, parser);
		if (hasErrors(parser)) {
			throw getExceptions(parser).get(0);
		}

		return generatePrologTerm(sc, parser).getSentences().get(0).toString();
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
