package de.prob.ltl;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import de.prob.parserbase.ProBParserBase;
import de.prob.parserbase.UnparsedParserBase;



public class PatternDefinitionTest extends AbstractLtlParserTest {

	private static UnparsedParserBase parserBase;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		parserBase = new UnparsedParserBase("expression", "predicate", "transition_predicate");

	}

	@Override
	public ProBParserBase getProBParserBase() {
		return parserBase;
	}

	public void assertEquals(String expected, String input) throws Exception {
		Assert.assertEquals(expected, parse(input));
	}

	@Test
	public void testDefinition() throws Exception {
		String def = "pattern_def(\"absence\",[\"a\"],globally(not(param(\"a\"))))";
		String call = "pattern_call(\"absence\",[true])";
		assertEquals(def + call, "def absence(a): G not a absence(true)");
	}

}
