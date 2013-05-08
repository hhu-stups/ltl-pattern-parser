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

		throwsRuntimeException("def f(a,b,c): a and b or c f(true, false, sink)");
		throwsRuntimeException("def f(): true f()");
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
		throwsRuntimeException("def not(a): a not(true)");
		throwsRuntimeException("def f(not): not f(true)");
		throwsRuntimeException("def and(a): a and(true)");
		throwsRuntimeException("def f(and): and f(true)");
		throwsRuntimeException("def or(a): a or(true)");
		throwsRuntimeException("def f(or): or f(true)");
		throwsRuntimeException("def U(a): a U(true)");
		throwsRuntimeException("def f(U): U f(true)");
		throwsRuntimeException("def G(a): a G(true)");
		throwsRuntimeException("def f(G): G f(true)");
		throwsRuntimeException("def GF(a): a GF(true)");
		throwsRuntimeException("def f(GF): GF f(false)");
		throwsRuntimeException("def true(a): a true(true)");
		throwsRuntimeException("def f(true): true f(false)");

		throwsRuntimeException("def f(a,a): a f(false,true)");

		throwsRuntimeException("def");
		throwsRuntimeException("def ");
		throwsRuntimeException("def abc");
		throwsRuntimeException("def abc()");
		throwsRuntimeException("def abc():");
		throwsRuntimeException("def abc(a):");
	}

	@Test
	public void testWrongCall() throws Exception {
		throwsRuntimeException("def f(a): a f()");
		throwsRuntimeException("def f(a): a f(true, false)");
		throwsRuntimeException("def f(a, b): a or b f(false)");
		throwsRuntimeException("def f(a): a g(false)");
	}

	@Test
	public void testVarUsageOutsidePattern() throws Exception {
		throwsRuntimeException("a and true");
		throwsRuntimeException("a");
		throwsRuntimeException("def f(a): a a");
	}

	@Test
	public void testRecursiveDefinitionCall() throws Exception {
		throwsRuntimeException("def f(a): f(a) or false f(true)");
		throwsRuntimeException("def f(a): a def f(b): f(b) f(true)");
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

	class WarningListenerTester implements WarningListener {

		int warningCount = 0;

		@Override
		public void warning(String message, Symbol... symbols) {
			warningCount++;
		}

	}

}
