package de.prob.ltl;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import de.prob.ltl.parser.symboltable.Symbol;
import de.prob.ltl.parser.warning.WarningListener;
import de.prob.parserbase.ProBParserBase;
import de.prob.parserbase.UnparsedParserBase;



public class PatternDefinitionTest extends AbstractLtlParserTest {

	private static UnparsedParserBase parserBase;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		parserBase = new UnparsedParserBase("expression", "predicate", "transition_predicate");
	}

	@Override
	public ProBParserBase getProBParserBase() {
		return parserBase;
	}

	public void assertEquals(String expected, String input) throws Exception {
		Assert.assertEquals(expected, parse(input));
	}

	@Test
	public void testDefinition() throws Exception {
		String def = "pattern_def(\"absence\",1,[\"a\"],globally(not(var(\"a\"))))";
		String call = "pattern_call(\"absence\",1,[true])";
		assertEquals(def + call, "def absence(a): G not a absence(true)");
		assertEquals(call + def, "absence(true) def absence(a): G not a");
	}

	@Test
	public void testDefinitionArgs() throws Exception {
		String def = "pattern_def(\"f\",1,[\"a\"],var(\"a\"))";
		String call = "pattern_call(\"f\",1,[true])";
		assertEquals(def + call, "def f(a): a f(true)");

		def = "pattern_def(\"f\",2,[\"a\",\"b\"],and(var(\"a\"),var(\"b\")))";
		call = "pattern_call(\"f\",2,[true,false])";
		assertEquals(def + call, "def f(a,b): a and b f(true, false)");

		throwsException("def f(a,b,c): a and b or c f(true, false, sink)");
		throwsException("def f(): true f()");
	}

	@Test
	public void testCallInDefintion() throws Exception {
		String def = "pattern_def(\"f\",1,[\"a\"],pattern_call(\"f\",2,[var(\"a\"),false]))";
		String def2 = "pattern_def(\"f\",2,[\"a\",\"b\"],or(var(\"a\"),var(\"b\")))";
		String call = "pattern_call(\"f\",1,[true])";
		assertEquals(def + def2 + call, "def f(a): f(a, false) def f(a, b): a or b f(true)");
		assertEquals(def2 + def + call, "def f(a, b): a or b def f(a): f(a, false) f(true)");
	}

	@Test
	public void testForbiddenDefintion() throws Exception {
		throwsException("def not(a): a not(true)");
		throwsException("def f(not): not f(true)");
		throwsException("def and(a): a and(true)");
		throwsException("def f(and): and f(true)");
		throwsException("def or(a): a or(true)");
		throwsException("def f(or): or f(true)");
		throwsException("def U(a): a U(true)");
		throwsException("def f(U): U f(true)");
		throwsException("def G(a): a G(true)");
		throwsException("def f(G): G f(true)");
		throwsException("def GF(a): a GF(true)");
		throwsException("def f(GF): GF f(false)");
		throwsException("def true(a): a true(true)");
		throwsException("def f(true): true f(false)");

		throwsException("def f(a,a): a f(false,true)");

		throwsException("def");
		throwsException("def ");
		throwsException("def abc");
		throwsException("def abc()");
		throwsException("def abc():");
		throwsException("def abc(a):");
	}

	@Test
	public void testWrongCall() throws Exception {
		throwsException("def f(a): a f()");
		throwsException("def f(a): a f(true, false)");
		throwsException("def f(a, b): a or b f(false)");
		throwsException("def f(a): a g(false)");
	}

	@Test
	public void testVarUsageOutsidePattern() throws Exception {
		throwsException("a and true");
		throwsException("a");
		throwsException("def f(a): a a");
	}

	@Test
	public void testRecursiveDefinitionCall() throws Exception {
		throwsException("def f(a): f(a) or false f(true)");
		throwsException("def f(a): a def f(b): f(b) f(true)");
	}

	@Test
	public void testUnusedVarWarning() throws Exception {
		WarningListenerTester listener = new WarningListenerTester();
		parse("def absence(a): true absence(true)", listener);
		Assert.assertEquals(1, listener.warningCount);

		listener.warningCount = 0;
		parse("def absence(a, b): true absence(true, false)", listener);
		Assert.assertEquals(2, listener.warningCount);
	}

	@Test
	public void testRedefinedPatternWarning() throws Exception {
		WarningListenerTester listener = new WarningListenerTester();
		parse("def f(a): a def f(b): b f(true)", listener);
		Assert.assertEquals(1, listener.warningCount);
	}

	@Test
	public void testScopes() throws Exception {
		parse("def f(x): x f(true)");
		parse("def f<global>(x): x f<global>(true)");
		parse("def f<before>(x): x f<before>(true)");
		parse("def f<after>(x): x f<after>(true)");
		parse("def f<between>(x): x f<between>(true)");
		parse("def f<until>(x): x f<until>(true)");

		throwsException("def f<>(x): x f(true)");
		throwsException("def f<a>(x): x f(true)");
		throwsException("def f(x): x f<>(true)");
		throwsException("def f(x): x f<a>(true)");
		throwsException("def f<global>(x): x f<until>(true)");
	}

	class WarningListenerTester implements WarningListener {

		int warningCount = 0;

		@Override
		public void warning(String message, Symbol... symbols) {
			warningCount++;
		}

	}

}
