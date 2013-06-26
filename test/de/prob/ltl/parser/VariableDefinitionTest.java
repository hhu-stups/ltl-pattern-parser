package de.prob.ltl.parser;

import org.junit.Assert;
import org.junit.Test;

public class VariableDefinitionTest extends AbstractParserTest {

	public void assertEquals(String expected, String input) throws Exception {
		Assert.assertEquals(expected, parse(input));
	}

	@Test
	public void testDefinitionSimple() throws Exception {
		parse("var v: true true");
		parse("var v: true v");
		parse("var s: false var v: s v");
	}

	@Test
	public void testAssignment() throws Exception {
		parse("var v: true v: false v");
		parse("var v: true v: v or false v");
	}

}
