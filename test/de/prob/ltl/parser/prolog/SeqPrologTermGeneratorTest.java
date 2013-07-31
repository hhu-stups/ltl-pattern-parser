package de.prob.ltl.parser.prolog;

import org.junit.Assert;
import org.junit.Test;

import de.prob.ltl.parser.AbstractParserTest;

public class SeqPrologTermGeneratorTest extends AbstractParserTest {

	// Helper
	public void assertEquals(String expected, String actual) {
		//System.out.println(expected);
		String a = parseToString(expected);
		String b = parseToString(actual);
		Assert.assertEquals(a, b);
	}

	// Tests
	@Test
	public void testSeqCall() {
		assertEquals("true & XF(false)", "seq s: (true, false) seq(s)");
		assertEquals("true & XF(false & XF(deadlock))", "seq s: (true, false, deadlock) seq(s)");
		assertEquals("true & !sink & X(!sink U false)", "seq s: (true, false without sink) seq(s)");
		assertEquals("true & !sink & X(!sink U (false & X(!sink U deadlock)))", "seq s: (true, false, deadlock without sink) seq(s)");

		assertEquals("true & !sink & X(!sink U false)", "seq s: (true, false) seq(s without sink)");
		assertEquals("true & !deadlock & !sink & X(!deadlock & !sink U false)", "seq s: (true, false without deadlock) seq(s without sink)");

		assertEquals("true & XF(false)", "seq(true, false)");
		assertEquals("true & !sink & X(!sink U false)", "seq(true, false without sink)");

		assertEquals("true & XF(false)", "seq((true, false))");
		assertEquals("true & !sink & X(!sink U false)", "seq((true, false without sink))");
	}

	@Test
	public void testSeqCallSubSeq() {
		assertEquals("(true & XF(false)) & XF(sink)", "seq((true, false), sink)");

		assertEquals("true & XF(false & XF(sink))", "seq((true, false), sink)");
	}

	@Test
	public void testSeqDefinition() {
		assertEquals("true", "seq s: (true, false) true");
	}

}
