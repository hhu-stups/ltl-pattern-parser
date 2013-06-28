package de.prob.ltl.parser;

import org.junit.Assert;
import org.junit.Test;

public class ExtendedPrologGeneratorTest extends AbstractParserTest {

	@Test
	public void testGenerateSimple() {
		Assert.assertEquals("false", parse("def pattern(): true false"));
		Assert.assertEquals("false", parse("false def pattern(): true"));
		Assert.assertEquals("true", parse("def pattern(): true pattern()"));
		Assert.assertEquals("or(true,false)", parse("def pattern(): true pattern() or false"));
	}

	@Test
	public void testParameter() {
		Assert.assertEquals("true", parse("def pattern(x): true pattern(false)"));
		Assert.assertEquals("false", parse("def pattern(x): x pattern(false)"));
		Assert.assertEquals("or(true,false)", parse("def pattern(x): true or x pattern(false)"));
		Assert.assertEquals("or(or(true,false),ap(sink))", parse("def pattern(x): true or x pattern(false) or sink"));
	}

	@Test
	public void testVars() {
		Assert.assertEquals("true", parse("var x: true def pattern(): x pattern()"));
		//Assert.assertEquals("true", parse("var x: true def pattern(y): y pattern(x)"));
		/*Assert.assertEquals("true", parse("def pattern(y): y var x: true pattern(x)"));
		Assert.assertEquals("true", parse("var x: true x"));
		Assert.assertEquals("false", parse("var x: true x: false x"));
		Assert.assertEquals("or(true,false)", parse("var x: true x: x or false x"));
		Assert.assertEquals("true", parse("var x: true var y: x y"));
		Assert.assertEquals("or(true,false)", parse("var x: true var y: x y: y or false y"));*/
		//System.out.println(parse("def pattern1(n): pattern2(n) def pattern2(n): true pattern1(true)"));
	}

}
