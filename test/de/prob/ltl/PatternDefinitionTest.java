package de.prob.ltl;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

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
		throwsRuntimeException("def GF(a): a GF(true)");
		throwsRuntimeException("def true(a): a true(true)");
		throwsRuntimeException("def f(true): true f(false)");
		throwsRuntimeException("def f(GF): GF f(false)");
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
	}

}
