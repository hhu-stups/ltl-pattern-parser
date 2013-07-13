package de.prob.ltl.parser.semantic;

import org.junit.Test;

import de.prob.ltl.parser.AbstractParserTest;

public class ExtendedSematicTest extends AbstractParserTest {

	@Test
	public void testDefineAndCallVariable() throws Exception {
		parse("var x: true x");
		throwsException("var x: (1) x");
		parse("var s: true var x: s x");
		throwsException("var x: true s");
		throwsException("var x: x x");
		throwsException("var x: s var s: true x");

		parse("num x: 1 true");
		parse("num x: (1) true");
		parse("num s: 1 num x: s true");
		parse("num s: 1 num x: (s) true");
		throwsException("var s: true num x: (s) true");
		throwsException("num x: 1 s");
		throwsException("num x: 1 x");
		throwsException("num x: x true");
		throwsException("num x: s num s: 1 true");

		throwsException("num x: 1 var s: x s");
		throwsException("var s: true num x: s s");
		throwsException("var s: true num x: s x");

		throwsException("var s: 1 s");
		throwsException("num s: true true");

		throwsException("var x: true var x: false x");
		throwsException("num x: 1 num x: 2 true");
		throwsException("var x: true num x: 1 x");
		throwsException("num x: 1 var x: true x");
	}

	@Test
	public void testAssignVariable() throws Exception {
		parse("var x: true x: false sink");
		throwsException("x: true sink");
		throwsException("var x: true s: false sink");

		parse("var x: true x: x sink");
		parse("var x: true x: x or false sink");

		parse("var x: true var y: false x: y sink");
		parse("var x: true var y: false x: x or y sink");
		throwsException("var x: true x: y var y: false sink");

		parse("num x: 1 x: 2 sink");
		throwsException("x: 1 sink");
		throwsException("num x: 1 s: 2 sink");

		parse("num x: 1 x: x sink");

		parse("num x: 1 num y: 2 x: y sink");
		throwsException("num x: 1 x: y num y: 2 sink");

		throwsException("var x: true x: 1 sink");
		throwsException("num x: 1 x: true sink");
		throwsException("var x: true num n: 1 x: n sink");
		throwsException("var x: true num n: 1 n: x sink");

		parse("var x: true var y: false x: y sink");
		parse("var x: true var y: false x: (y) sink");
		parse("num x: 1 num y: 2 x: y sink");
		parse("num x: 1 num y: 2 x: (y) sink");

		throwsException("var x: true num y: 2 x: y sink");
		throwsException("var x: true num y: 2 x: (y) sink");
		throwsException("num x: 1 var y: true x: y sink");
		throwsException("num x: 1 var y: true x: (y) sink");
	}

	@Test
	public void testDefineAndCallPattern() throws Exception {
		parse("def pattern(): true false");
		parse("def pattern(): true pattern()");

		parse("def pattern(): true pattern<global>()");
		parse("def <global> pattern(): true pattern()");
		parse("def <global> pattern(): true pattern<global>()");
		parse("def <before r> pattern(): true pattern<before true>()");
		parse("def <after q> pattern(): true pattern<after true>()");
		parse("def <between q and r> pattern(): true pattern<between true and false>()");
		parse("def <after q until r> pattern(): true pattern<after true until false>()");

		throwsException("def pattern(): true a()");
		throwsException("def pattern(): true pattern<before true>()");
		throwsException("def <before r> pattern(): true pattern()");

		parse("def a(): true def b(): true true");
		parse("def a(): true def b(): true a() or b()");
		parse("def a(): true def b(): true b() or a()");
		throwsException("def a(): true def a(): true true");
		parse("def <before r> a(): true def a(): true true");

		throwsException("def <before r> a(): true a<before r>()");
		throwsException("def <before r> a(x): true a<before r>(true)");
		throwsException("def <before r> a(x): true a<before true>(r)");
		throwsException("def <before r> a(x): true a<before x>(true)");
		throwsException("def <before r> a(x): true a<before true>(x)");
	}

	@Test
	public void testLoops() throws Exception {
		parse("count 1 up to 2: var x: true x: false end false");
		parse("count 2 down to 1: var x: true x: false end false");
		parse("count (1) up to (2): var x: true x: false end false");
		parse("count (2) down to (1): var x: true x: false end false");

		throwsException("count x up to 2: num x: 1 end false");
		throwsException("count 1 up to x: num x: 1 end false");
		throwsException("count i: 1 up to i: num x: 1 end false");
		throwsException("count i: i up to 2: num x: 1 end false");

		throwsException("count 1 up to 2: x: false end false");
		throwsException("count x up to 2: var x: false end false");
		throwsException("count 1 up to y: var x: false end false");

		parse("num x: 1 num y: 2 count x up to y: var s: false end false");
		parse("num x: 1 num y: 2 count (x) up to (y): var s: false end false");
		parse("num x: 1 num y: 2 count (x) up to (y): x: 3 end false");
		throwsException("num x: 1 num y: 2 count x up to y: var x: false end false");
		throwsException("num x: 1 var y: 2 count x up to y: var s: false end false");

		parse("count i: 1 up to 2: var x: true x: false end false");
		parse("count i: 2 down to 1: var x: true x: false end false");
		parse("count i: 1 up to 2: i: 0 end false");
		parse("count i: 2 down to 1: i: 0 end false");
		throwsException("count i: 1 up to 2: i: true end false");
		throwsException("count i: 1 up to 2: num i: 0 end false");
		throwsException("count i: 2 down to 1: var i: true end false");
		throwsException("var i: true count i: 2 down to 1: i: 0 end false");
		throwsException("num i: 1 count i: 2 down to 1: i: 0 end false");
		throwsException("count i: 1 up to 2: var x: true end i: 0 false");
	}

}
