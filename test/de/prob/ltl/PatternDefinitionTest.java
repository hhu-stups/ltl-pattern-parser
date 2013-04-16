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
		assertEquals("pattern_def(\"absence\",[\"a\"],globally(not(param(\"a\"))))true", "def absence(a): G not a true");
	}

}
