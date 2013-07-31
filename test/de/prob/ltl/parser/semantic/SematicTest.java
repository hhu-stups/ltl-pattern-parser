package de.prob.ltl.parser.semantic;

import org.junit.Test;

import de.prob.ltl.parser.AbstractParserTest;

public class SematicTest extends AbstractParserTest {

	@Test
	public void testDefinitionPatternParameter() throws Exception {
		String pattern1 = "def pattern(x, y): true ";
		String pattern2 = "def pattern2(x, y): true ";
		String expr = "true ";
		parse(pattern1 +  pattern2 + expr);
		parse("def pattern3(x, y): x or y true");
		throwsException("def pattern3(x, y): x y");
	}

	@Test
	public void testDefinitionPatternError() throws Exception {
		String pattern1 = "def pattern(x, x): true ";
		String pattern2 = "def pattern(x, y): z ";
		String expr = "true ";
		throwsException(pattern1 + expr);
		throwsException(pattern2 + expr);
	}

	@Test
	public void testCallPattern() throws Exception {
		String pattern1 = "def pattern(): true ";
		String pattern2 = "def pattern2(): pattern() ";
		String expr = "pattern2() ";
		parse(pattern1 +  pattern2 + expr);
		parse(pattern2 +  pattern1 + expr);
		throwsException(expr + pattern2 +  pattern1);
		throwsException(expr);
	}

	@Test
	public void testWrongCall() throws Exception {
		throwsException("def f(a): a f()");
		throwsException("def f(a): a f(true, false)");
		throwsException("def f(a, b): a or b f(false)");
		throwsException("def f(a): a g(false)");
	}

	@Test
	public void testDefinitionVar() throws Exception {
		String var1 = "var x: true ";
		String var2 = "var y: false ";
		String expr = "true ";
		parse(var1 + var2 + expr);
		throwsException(var1 + var1 + expr);
	}

	@Test
	public void testVarAssignment() throws Exception {
		String var = "var x: true ";
		String assign = "x: false ";
		String expr = "true ";
		parse(var + assign + expr);
		throwsException(assign + expr);
		throwsException(assign + var + expr);
	}

	@Test
	public void testVarCall() throws Exception {
		String var1 = "var x: true ";
		String var2 = "var y: x ";
		String var3 = "var z: z ";
		String var4 = "var z: false ";
		String assign = "x: z ";
		String expr = "true ";
		parse(var1 + var2 + expr);
		parse(var1 + var4 + assign + expr);
		parse(var1 + var2 + "x");
		throwsException(var2 + var1 + expr);
		throwsException(var4 + assign + var1 + expr);
		throwsException(var2 + expr);
		throwsException(var3 + expr);
	}

	@Test
	public void testPatternsAndVars() throws Exception {
		String pattern1 = "def pattern(): true ";
		String pattern2 = "def pattern(x): true ";
		String pattern4 = "def pattern(): x ";
		String pattern5 = "def pattern(): var x: true true ";

		String expr = "true ";

		String var1 = "var x: true ";
		parse(var1 + pattern1 + expr);
		parse(pattern1 + var1 + expr);

		parse(var1 + pattern2 + expr);
		parse(pattern2 + var1 + expr);

		throwsException(var1 + pattern4 + expr);
		throwsException(pattern4 + var1 + expr);

		parse(var1 + pattern5 + expr);
		parse(pattern5 + var1 + expr);

		String var2 = "var x: pattern() ";
		String var3 = "var x: pattern(true) ";
		parse(var2 + pattern1 + expr);
		parse(pattern1 + var2 + expr);

		parse(var3 + pattern2 + expr);
		parse(pattern2 + var3 + expr);

		// TODO cycle detection throwsException(var2 + pattern4 + expr);
		throwsException(pattern4 + var2 + expr);

		parse(var2 + pattern5 + expr);
		parse(pattern5 + var2 + expr);
	}

	@Test
	public void testLoop() throws Exception {
		String loop1 = "count 1 up to 2: var x: true end ";
		String pattern1 = "def pattern(): " + loop1 + " true ";
		String pattern2 = "def pattern(x): " + loop1 + " true ";
		String expr = "true ";
		parse(pattern1 + expr);
		throwsException(pattern2 + expr);

		String loop2 = "count 1 up to 2: x: true end ";
		String pattern4 = "def pattern(): " + loop2 + " true ";
		String pattern5 = "def pattern(x): " + loop2 + " true ";
		throwsException(pattern4 + expr);
		parse(pattern5 + expr);

		String loop3 = "count 1 up to 5: var x: true x: x or false end ";
		String pattern7 = "def pattern(): " + loop3 + " true ";
		parse(pattern7 + expr);

		String varDef = "var x: true ";

		parse(varDef + pattern1 + expr);
		throwsException(varDef + pattern4 + expr);
		parse(varDef + pattern7 + expr);

		parse(pattern1 + varDef + expr);
		throwsException(pattern2 + varDef + expr);
		throwsException(pattern4 + varDef + expr);
		parse(pattern5 + varDef + expr);
		parse(pattern7 + varDef + expr);

		String varAssign = "x: true ";

		throwsException(pattern1 + varAssign + expr);
		throwsException(pattern2 + varAssign + expr);
		throwsException(pattern4 + varAssign + expr);
		throwsException(pattern5 + varAssign + expr);
		throwsException(pattern7 + varAssign + expr);
	}

	@Test
	public void testLoop2() throws Exception {
		parse("def pattern(): count 1 up to 2: var x: true end var x: false x true");
		throwsException("def pattern(): var x: false count 1 up to 2: var x: true end x true");

		parse("def pattern(x:num): count x up to 2: var s: true end true true");
		throwsException("def pattern(y): count x up to 2: var s: true end true true");
		throwsException("def pattern(): count x up to 2: var s: true end true true");
		throwsException("def pattern(): count 1 up to x: var s: true end true true");
	}

	@Test
	public void testNumVarPatternCall() throws Exception {
		parse("def pattern(n:num): true pattern(1)");
		parse("def pattern(n:num): true pattern(0)");
		parse("def pattern(n:num): true pattern(123)");
		throwsException("def pattern(n:num): true pattern(true)");
		throwsException("def pattern(n:num): true pattern(GF true)");
		throwsException("def pattern(n:num): true pattern({...})");

		throwsException("def pattern(n): true pattern(1)");
		throwsException("def pattern(n): true pattern(0)");
		throwsException("def pattern(n): true pattern(123)");
		parse("def pattern(n): true pattern(true)");
		parse("def pattern(n): true pattern(GF true)");
		parse("def pattern(n): true pattern({...})");

		throwsException("def pattern(n:num): var x: n pattern(1)");
		throwsException("def pattern(n:num): var x: true x: n pattern(1)");
		throwsException("def pattern(n:num): n: 2 pattern(1)");

		parse("def pattern1(n:num): pattern2(n) def pattern2(n:num): true pattern1(123)");
		throwsException("def pattern1(n): pattern2(n) def pattern2(n:num): true pattern1(true)");
	}

	@Test
	public void testNumVarLoop() throws Exception {
		parse("def pattern(n:num): count n up to 2: var s: true end true pattern(1)");
		parse("def pattern(n:num): count 1 up to n: var s: true end true pattern(1)");
		parse("def pattern(n:num): count n up to n: var s: true end true pattern(1)");
		throwsException("def pattern(n): count n up to 2: var s: true end true pattern(true)");
		throwsException("def pattern(n): count 1 up to n: var s: true end true pattern(GF true)");
		throwsException("def pattern(n): count n up to n: var s: true end true pattern({...})");
	}

	@Test
	public void testNumVarAssignment() throws Exception {
		parse("var x: true x");
		throwsException("var x: 1 x");
		parse("var x: true x: false x");
		throwsException("var x: true x: 1 x");

		throwsException("def pattern(n:num): count 1 up to 2: n: true end pattern(1)");
		throwsException("def pattern(n:num): count 1 up to 2: n: 2 end pattern(1)");
		throwsException("def pattern(n:num): n: true pattern(1)");
		throwsException("def pattern(n:num): n: 2 pattern(1)");
		throwsException("def pattern(n:num, x:num): n: x pattern(1, 2)");
	}

	@Test
	public void testSeqVarDefinition() throws Exception {
		parse("seq s: (true, false) true");
		parse("seq s: (true, false, sink) true");
		parse("seq s: (true, false without sink) true");

		parse("seq s: (true, false) seq t: s true");
		parse("seq s: (true, false) seq t: s without sink true");
		parse("seq s: (true, false) seq t: (true, false without s) true");
		parse("seq s: (true, false) seq t: (s, false) true");

		parse("var v: true seq s: (v, false) true");
		parse("var v: true seq s: (true, false without v) true");

		throwsException("seq s: true true");
		throwsException("seq s: 1 true");
		throwsException("seq s: (true) true");
		throwsException("seq s: (1) true");
		throwsException("seq s: (1, true) true");
		throwsException("num n: true seq s: (n, false) true");
		throwsException("num n: true seq s: (true, false without n) true");

		throwsException("var v: true seq s: v without true true");
		throwsException("num n: true seq s: n without true true");
		throwsException("seq s: s without true true");
		throwsException("seq s: (true, false without s) true");
		throwsException("seq s: s true");

		throwsException("seq s: (true, false) without sink true");
		throwsException("seq s: (true, false) s");

		parse("seq s: ((true, (sink, deadlock)), false) seq t: (s, false) true");
	}

	@Test
	public void testSeqVarAssignment() throws Exception {
		parse("seq s: (true, false) s: (true, false) true");
		parse("seq s: (true, false) s: (true, false without sink) true");

		parse("seq s: (true, false) seq t: s t: s true");
		parse("seq s: (true, false) seq t: s t: s without sink true");
		parse("seq s: (true, false) seq t: s t: (true, false without s) true");
		parse("seq s: (true, false) seq t: s t: (s, false) true");
		parse("seq s: (true, false) seq t: s t: (t, false) true");
		parse("seq s: (true, false) seq t: s t: t true");

		parse("var v: true seq s: (true, false) s: (v, false) true");
		parse("var v: true seq s: (true, false) s: (true, false without v) true");

		throwsException("seq s: (true, false) s: true true");
		throwsException("seq s: (true, false) s: 1 true");
		throwsException("seq s: (true, false) s: (true) true");
		throwsException("seq s: (true, false) s: (1) true");
		throwsException("seq s: (true, false) s: (1, true) true");
		throwsException("num n: true seq s: (true, false) s: (n, false) true");
		throwsException("num n: true seq s: (true, false) s: (true, false without n) true");

		throwsException("var v: true seq s: (true, false) s: v without true true");
		throwsException("num n: true seq s: (true, false) s: n without true true");
		parse("seq s: (true, false) s: s without true true");
		parse("seq s: (true, false) s: (true, false without s) true");

		throwsException("seq s: (true, false) s: (true, false) without sink true");
	}

	@Test
	public void testSeqPatternCall() throws Exception {
		String pattern1 = " def p(s:seq): true ";
		String pattern2 = " def p(v): true ";
		String pattern3 = " def p(n:num): true ";

		parse(pattern1 + "p((true, false))");
		parse(pattern1 + "p((true, false without sink))");
		parse(pattern1 + "seq s: (true, false) p(s)");
		parse(pattern1 + "seq s: (true, false) p(s without sink)");

		throwsException(pattern1 + "p(true)");
		throwsException(pattern1 + "p(1)");
		throwsException(pattern1 + "num n: 1 p(n)");
		throwsException(pattern1 + "var v: true p(v)");

		throwsException(pattern2 + "p((true, false))");
		throwsException(pattern2 + "p((true, false without sink))");
		throwsException(pattern2 + "seq s: (true, false) p(s)");
		throwsException(pattern2 + "seq s: (true, false) p(s without sink)");

		throwsException(pattern3 + "p((true, false))");
		throwsException(pattern3 + "p((true, false without sink))");
		throwsException(pattern3 + "seq s: (true, false) p(s)");
		throwsException(pattern3 + "seq s: (true, false) p(s without sink)");

		throwsException("def p(s:seq): s p((true, false))");
	}

	@Test
	public void testSeqScopeCall() throws Exception {
		throwsException("before(true, (true, false))");
		throwsException("before(true, (true, false without sink))");
		throwsException("seq s: (true, false) before(true, s)");
		throwsException("seq s: (true, false) before(true, s without sink)");

		throwsException("before((true, false), false)");
		throwsException("before((true, false without sink), false)");
		throwsException("seq s: (true, false) before(s, false)");
		throwsException("seq s: (true, false) before(s without sink, false)");
	}

	@Test
	public void testSeqLoop() throws Exception {
		throwsException("loop (true, false) up to 5: var v: true end true");
		throwsException("loop 1 up to (true, false): var v: true end true");
		throwsException("seq s: (true, false) loop 1 up to s: var v: true end true");
		throwsException("seq s: (true, false) loop s up to 5: var v: true end true");
	}

	@Test
	public void testSeqCall() throws Exception {
		parse("seq s: (true, false) seq(s)");
		parse("seq s: (true, false) seq(s without sink)");
		throwsException("var s: true seq(s)");
		throwsException("var s: true seq(s without sink)");
		throwsException("num s: 1 seq(s)");
		throwsException("num s: 1 seq(s without sink)");

		parse("seq(true, false)");
		parse("seq(true, false without sink)");
		throwsException("seq(1, false)");
		throwsException("seq(true, false without 1)");
		throwsException("num s: 1 seq(s, false without sink)");
		throwsException("num s: 1 seq(true, false without s)");

		parse("seq((true, false))");
		parse("seq((true, false without sink))");
		throwsException("seq((true, false) without sink)");

		parse("true or seq(true, false)");
		parse("GF seq(true, false)");
		parse("def p(): seq(true, false) p()");
		parse("def p(s:seq): seq(s) p((true, false))");
		parse("before(true, seq(true, false))");

		parse("var s: seq(true, false) s");
		throwsException("num s: seq(true, false) true");
		throwsException("seq s: seq(true, false) true");
		parse("seq s: (true, seq(true, false)) true");

		throwsException("loop seq(true, false) up to 5: var v: true end true");
		throwsException("loop 1 up to seq(true, false): var v: true end true");
	}

	/*@Test
	public void testRecursiveDefinitionCall() throws Exception {
		throwsException("def f(a): f(a) or false f(true)");
	}*/

	/* TODO warning listener
	@Test
	TODO public void testUnusedVarWarning() throws Exception {
		WarningListenerTester listener = new WarningListenerTester();
		parse("def absence(a): true absence(true)", listener);
		Assert.assertEquals(1, listener.warningCount);

		listener.warningCount = 0;
		parse("def absence(a, b): true absence(true, false)", listener);
		Assert.assertEquals(2, listener.warningCount);
	}*/

}
