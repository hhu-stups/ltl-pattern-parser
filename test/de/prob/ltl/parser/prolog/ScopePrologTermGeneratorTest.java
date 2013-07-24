package de.prob.ltl.parser.prolog;

import org.junit.Assert;
import org.junit.Test;

import de.prob.ltl.parser.AbstractParserTest;

public class ScopePrologTermGeneratorTest extends AbstractParserTest {

	// Helper
	public void assertEquals(String expected, String actual) {
		System.out.println(expected);
		String a = parseToString(expected);
		String b = parseToString(actual);
		Assert.assertEquals(a, b);
	}

	// Tests
	@Test
	public void testBeforeScopeSimpleP() {
		String patternA = " def p(): true ";
		String patternB = " def p(x): x ";
		String var = " var x: true ";

		assertEquals("F(sink) => true", "before(sink, true)");
		assertEquals("F(sink) => e(...)", "before(sink, e(...))");
		assertEquals("F(sink) => [...]", "before(sink, [...])");
		assertEquals("F(sink) => {...}", "before(sink, {...})");
		assertEquals(patternA + "F(sink) => p()", 		patternA + "before(sink, p())");
		assertEquals(patternB + "F(sink) => p(true)", 	patternB + "before(sink, p(true))");
		assertEquals(var +"F(sink) => x", 				var + "before(sink, x)");
		assertEquals(var + patternB + "F(sink) => p(x)", var + patternB + "before(sink, p(x))");
	}

	@Test
	public void testBeforeScopeOperatorsP() {
		assertEquals("F(sink) => true U sink", 				"before(sink, G(true))");
		assertEquals("F(sink) => !sink U (true & !sink)", 	"before(sink, F(true))");
		//assertEquals("F(sink) => true", 	"before(sink, X(true))");
		//assertEquals("F(sink) => true", 	"before(sink, H(true))");
		//assertEquals("F(sink) => true", 	"before(sink, O(true))");
		//assertEquals("F(sink) => true", 	"before(sink, Y(true))");

		assertEquals("F(sink) => (true & !sink) U (false & !sink)", 	"before(sink, true U false)");
		assertEquals("F(sink) => false U (sink | (false & true))", 		"before(sink, true R false)");
		assertEquals("F(sink) => true U (sink | false)", 				"before(sink, true W false)");
		//assertEquals("F(sink) => true", 	"before(sink, true S false)");
		//assertEquals("F(sink) => true", 	"before(sink, true T false)");
	}

	@Test
	public void testBeforePatterns() {
		assertEquals("F(sink) => !true U sink", 			"before(sink, G(!true))");	// absence
		assertEquals("F(sink) => !sink U (true & !sink)", 	"before(sink, F(true))");	// existence
		assertEquals("F(sink) => true U sink", 				"before(sink, G(true))");	// universality
		assertEquals("F(sink) => !true U (sink | false)", 	"before(sink, !true W false)");	// precedence
		assertEquals("F(sink) => (true => !sink U (false & !sink)) U sink", "before(sink, G(true => F(false)))");	// response
	}

}
