package de.prob.ltl.parser.prolog;

import org.junit.Test;

import de.prob.ltl.parser.AbstractParserTest;

public class PrologTermGeneratorTest extends AbstractParserTest {

	@Test
	public void testCollectPatternSimple() {
		System.out.println(parseToString("def pattern(x): x pattern(true)"));
		System.out.println(parseToString("def pattern(): true GF pattern() or false"));
		System.out.println(parseToString("def pattern(x): true GF pattern(true) or false"));
		System.out.println(parseToString("def pattern(x): true pattern(pattern(true))"));
		System.out.println(parseToString("def pattern(x, y): true GF pattern(true,false) "));
		System.out.println(parseToString("def pattern(x, y): x or y GF pattern(true,false) "));

		System.out.println(parseToString(" var y: true def pattern(x): y: false x pattern(y) or y"));
	}

	@Test
	public void testCollectVariableSimple() {
		System.out.println(parseToString("var x: true x or false"));
		System.out.println(parseToString("var x: true x: false x or false"));
		System.out.println(parseToString("var x: true x: x and false x"));
	}

	@Test
	public void testCollectLoopSimple() {
		System.out.println(parseToString("def pattern(): var x: false loop 1 up to 3: x: x or true end x pattern()"));
		System.out.println(parseToString("def pattern(n:num): var x: false loop n down to 1: x: x or true end x pattern(3)"));
	}

}
