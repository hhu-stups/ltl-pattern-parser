package de.prob.ltl;

import org.junit.Assert;
import org.junit.BeforeClass;

import de.prob.parserbase.ProBParserBase;
import de.prob.parserbase.UnparsedParserBase;
import de.prob.prolog.term.PrologTerm;


public class AbstractOldParserCompareTest extends AbstractLtlParserTest {

	enum ExceptionCause {
		DownwardIncompatible,
		Deprecated,
		Unsupported
	};

	private static UnparsedParserBase parserBase;
	private static de.be4.ltl.core.parser.LtlParser oldParser;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		parserBase = new UnparsedParserBase("expression", "predicate", "transition_predicate");
		oldParser = new de.be4.ltl.core.parser.LtlParser(parserBase);
	}

	@Override
	public ProBParserBase getProBParserBase() {
		return parserBase;
	}

	// Helper
	protected String parseOld(String input) throws Exception {
		PrologTerm term = oldParser.generatePrologTerm(input, "current");
		return term.toString();
	}

	public void assertEquals(String expected, String ... inputs) throws Exception {
		for (String input : inputs) {
			Assert.assertEquals(expected, parse(input));
			Assert.assertEquals(expected, parseOld(input));
		}
	}

	public void throwsException(String expected, String input, ExceptionCause cause) {
		try {
			Assert.assertEquals(expected, parse(input, true));
			if (!cause.equals(ExceptionCause.DownwardIncompatible)) {
				Assert.fail("Exception for new parser version should have been thrown. (Input: "+input+")");
			}
		} catch(RuntimeException ex) {
			if (cause.equals(ExceptionCause.DownwardIncompatible)) {
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

}
