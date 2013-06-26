package de.prob.ltl.parser;

import org.junit.Assert;
import org.junit.Test;

public class LtlPrologTermGeneratorTest extends AbstractParserTest {

	@Test
	public void testAtoms() {
		Assert.assertEquals("true", parse("true"));
		Assert.assertEquals("false", parse("false"));
		Assert.assertEquals("ap(sink)", parse("sink"));
		Assert.assertEquals("ap(deadlock)", parse("deadlock"));
		Assert.assertEquals("ap(stateid(current))", parse("current"));

		Assert.assertEquals("true", parse("(true)"));
		Assert.assertEquals("true", parse(" ( (true ) ) "));

		Assert.assertEquals("ap(predicate('...'))", parse("{...}"));
		Assert.assertEquals("ap(predicate(abc))", parse("{abc}"));
		Assert.assertEquals("ap(predicate(' a b c'))", parse("{ a b c}"));
		Assert.assertEquals("ap(predicate(' {}'))", parse("{ {}}"));

		Assert.assertEquals("action(transition_predicate('...'))", parse("[...]"));
		Assert.assertEquals("action(transition_predicate(abc))", parse("[abc]"));
		Assert.assertEquals("action(transition_predicate(' a b c'))", parse("[ a b c]"));
		Assert.assertEquals("action(transition_predicate(' []'))", parse("[ []]"));

		Assert.assertEquals("ap(enabled(transition_predicate('...')))", parse("e(...)"));
		Assert.assertEquals("ap(enabled(transition_predicate(abc)))", parse("e(abc)"));
		Assert.assertEquals("ap(enabled(transition_predicate(' a b c')))", parse("e( a b c)"));
		Assert.assertEquals("ap(enabled(transition_predicate(' ()')))", parse("e( ())"));
	}

	@Test
	public void testExpr() {
		Assert.assertEquals("not(true)", parse("not true"));
		Assert.assertEquals("not(true)", parse("! true"));
		Assert.assertEquals("not(true)", parse("!true"));
		Assert.assertEquals("not(true)", parse("not(true)"));

		Assert.assertEquals("globally(true)", parse("G true"));
		Assert.assertEquals("finally(true)", parse("F true"));
		Assert.assertEquals("next(true)", parse("X true"));
		Assert.assertEquals("historically(true)", parse("H true"));
		Assert.assertEquals("once(true)", parse("O true"));
		Assert.assertEquals("yesterday(true)", parse("Y true"));
		Assert.assertEquals("globally(finally(true))", parse("GF true"));

		Assert.assertEquals("until(true,false)", parse("true U false"));
		Assert.assertEquals("weakuntil(true,false)", parse("true W false"));
		Assert.assertEquals("release(true,false)", parse("true R false"));
		Assert.assertEquals("since(true,false)", parse("true S false"));
		Assert.assertEquals("trigger(true,false)", parse("true T false"));

		Assert.assertEquals("and(true,false)", parse("true and false"));
		Assert.assertEquals("and(true,false)", parse("true & false"));
		Assert.assertEquals("or(true,false)", parse("true or false"));
		Assert.assertEquals("or(true,false)", parse("true | false"));
		Assert.assertEquals("implies(true,false)", parse("true => false"));
	}

}
