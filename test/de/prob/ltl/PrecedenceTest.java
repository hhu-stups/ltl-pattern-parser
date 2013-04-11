package de.prob.ltl;

import org.junit.Assert;
import org.junit.Test;


public class PrecedenceTest extends AbstractOldParserCompareTest {

	@Test
	public void testAnd() throws Exception {
		// (), constant
		assertEquals("and(true,false)",
				"true & false",
				"(true & false)",
				"(true) & (false)");

		// not
		assertEquals("and(not(true),not(false))",
				"not true & not false",
				"(not true) & (not false)");
		assertEquals("and(not(not(true)),not(false))",
				"not not true & not false",
				"(not (not true)) & (not false)");
		assertEquals("not(and(not(true),not(false)))",
				"not (not true & not false)",
				"not ((not true) & (not false))");

		// and
		assertEquals("and(and(true,false),ap(sink))",
				"true & false & sink",
				"((true & false) & sink)");
		assertEquals("and(and(and(true,false),ap(sink)),ap(deadlock))",
				"true & false & sink & deadlock",
				"(((true & false) & sink) & deadlock)");

		// or
		assertEquals("or(and(true,false),ap(sink))",
				"true & false or sink",
				"(true & false) or sink");
		incompatiblePrecedence("or(ap(sink),and(true,false))",
				"sink or true & false",
				"sink or (true & false)");
		incompatiblePrecedence("or(and(ap(sink),ap(deadlock)),and(true,false))",
				"sink & deadlock or true & false",
				"(sink & deadlock) or (true & false)");
		incompatiblePrecedence("or(or(ap(sink),and(ap(deadlock),true)),false)",
				"sink or deadlock & true or false",
				"(sink or (deadlock & true)) or false");

		// implies
		assertEquals("implies(and(true,false),ap(sink))",
				"true & false => sink",
				"(true & false) => sink");
		assertEquals("implies(ap(sink),and(true,false))",
				"sink => true & false",
				"sink => (true & false)");
		assertEquals("implies(and(ap(sink),ap(deadlock)),and(true,false))",
				"sink & deadlock => true & false",
				"(sink & deadlock) => (true & false)");

		// Binary Ltl
		incompatiblePrecedence("until(and(true,false),ap(sink))",
				"true & false U sink",
				"(true & false) U sink");
		incompatiblePrecedence("release(ap(sink),and(true,false))",
				"sink R true & false",
				"sink R (true & false)");
		incompatiblePrecedence("trigger(and(ap(sink),ap(deadlock)),and(true,false))",
				"sink & deadlock T true & false",
				"(sink & deadlock) T (true & false)");

		// Unary Ltl
		assertEquals("and(globally(true),finally(false))",
				"G true & F false",
				"(G true) & (F false)");
		assertEquals("and(globally(true),false)",
				"G true & false",
				"(G true) & false");
		assertEquals("and(globally(finally(true)),false)",
				"GF true & false",
				"(GF true) & false");
	}

	@Test
	public void testOr() throws Exception {
		// (), constant
		assertEquals("or(true,false)",
				"true or false",
				"(true or false)",
				"(true) or (false)");

		// not
		assertEquals("or(not(true),not(false))",
				"not true or not false",
				"(not true) or (not false)");
		assertEquals("or(not(not(true)),not(false))",
				"not not true or not false",
				"(not (not true)) or (not false)");
		assertEquals("not(or(not(true),not(false)))",
				"not (not true or not false)",
				"not ((not true) or (not false))");

		// or
		assertEquals("or(or(true,false),ap(sink))",
				"true or false or sink",
				"((true or false) or sink)");
		assertEquals("or(or(or(true,false),ap(sink)),ap(deadlock))",
				"true or false or sink or deadlock",
				"(((true or false) or sink) or deadlock)");

		// implies
		assertEquals("implies(or(true,false),ap(sink))",
				"true or false => sink",
				"(true or false) => sink");
		assertEquals("implies(ap(sink),or(true,false))",
				"sink => true or false",
				"sink => (true or false)");
		assertEquals("implies(or(ap(sink),ap(deadlock)),or(true,false))",
				"sink or deadlock => true or false",
				"(sink or deadlock) => (true or false)");

		// Binary Ltl
		incompatiblePrecedence("until(or(true,false),ap(sink))",
				"true or false U sink",
				"(true or false) U sink");
		incompatiblePrecedence("release(ap(sink),or(true,false))",
				"sink R true or false",
				"sink R (true or false)");
		incompatiblePrecedence("trigger(or(ap(sink),ap(deadlock)),or(true,false))",
				"sink or deadlock T true or false",
				"(sink or deadlock) T (true or false)");

		// Unary Ltl
		assertEquals("or(globally(true),finally(false))",
				"G true or F false",
				"(G true) or (F false)");
		assertEquals("or(globally(true),false)",
				"G true or false",
				"(G true) or false");
		assertEquals("or(globally(finally(true)),false)",
				"GF true or false",
				"(GF true) or false");
	}

	// Helper
	public void incompatiblePrecedence(String expected, String incompatibleInput, String equivInput) throws Exception {
		Assert.assertEquals(expected, parse(incompatibleInput));
		String oldOutput = parseOld(incompatibleInput);
		if (expected.equals(oldOutput)) {
			Assert.fail("The old parser version output should differ from the expected output. (Input: "+incompatibleInput+")");
		} else {
			System.out.println("Incompatible input: " + incompatibleInput + " expected precedence: " + equivInput);
		}
		assertEquals(expected, equivInput);
	}

}