package de.prob.ltl.parser.prolog;

import org.junit.Assert;
import org.junit.Test;

import de.prob.ltl.parser.AbstractParserTest;

public class ExtendedPrologGeneratorTest extends AbstractParserTest {

	@Test
	public void testGenerateSimple() {
		Assert.assertEquals("false", parseToString("def pattern(): true false"));
		Assert.assertEquals("false", parseToString("false def pattern(): true"));
		Assert.assertEquals("true", parseToString("def pattern(): true pattern()"));
		Assert.assertEquals("or(true,false)", parseToString("def pattern(): true pattern() or false"));
	}

	@Test
	public void testParameter() {
		Assert.assertEquals("true", parseToString("def pattern(x): true pattern(false)"));
		Assert.assertEquals("false", parseToString("def pattern(x): x pattern(false)"));
		Assert.assertEquals("or(true,false)", parseToString("def pattern(x): true or x pattern(false)"));
		Assert.assertEquals("or(or(true,false),ap(sink))", parseToString("def pattern(x): true or x pattern(false) or sink"));
	}

	@Test
	public void testVars() {
		Assert.assertEquals("true", parseToString("var x: true def pattern(): x pattern()"));
		//Assert.assertEquals("true", parseToString("var x: true def pattern(y): y pattern(x)"));
		/*Assert.assertEquals("true", parseToString("def pattern(y): y var x: true pattern(x)"));
		Assert.assertEquals("true", parseToString("var x: true x"));
		Assert.assertEquals("false", parseToString("var x: true x: false x"));
		Assert.assertEquals("or(true,false)", parseToString("var x: true x: x or false x"));
		Assert.assertEquals("true", parseToString("var x: true var y: x y"));
		Assert.assertEquals("or(true,false)", parseToString("var x: true var y: x y: y or false y"));*/
		//System.out.println(parseToString("def pattern1(n): pattern2(n) def pattern2(n): true pattern1(true)"));
	}

}
