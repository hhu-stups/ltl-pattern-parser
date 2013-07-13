package de.prob.ltl.parser;

import org.junit.Test;

public class VariableDefinitionTest extends AbstractParserTest {

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
