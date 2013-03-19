package de.prob.ltl;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import de.prob.parserbase.ProBParserBase;
import de.prob.parserbase.UnparsedParserBase;
import de.prob.prolog.term.PrologTerm;


public class CompatibilityTest extends AbstractLtlParserTest {

	private static UnparsedParserBase parserBase;
	private static de.be4.ltl.core.parser.LtlParser oldParser;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		parserBase = new UnparsedParserBase("expression", "predicate", "transition_predicate");
		oldParser = new de.be4.ltl.core.parser.LtlParser(parserBase);
	}

	@Override
	public ProBParserBase getProBParserBase() {
		return parserBase;
	}

	// Helper
	protected String parseOld(String input) throws Exception {
		PrologTerm term = oldParser.generatePrologTerm(input, "current");
		return term.toString();
	}

	@Test
	public void testConstants() throws Exception {
		for (String input : new String[] {
				" true",
				"false ",
				"sink",
				"deadlock", "current" }) {
			assertEquals(parseOld(input), parse(input));
		}
	}

	@Test
	public void testUnaryOp() throws Exception {
		for (String input : new String[] {
				"nottrue",
				"not true",
				"not \n true",
				"not \t true",
				"not \r true",
				"Xtrue",
				"X (false)" ,
				"F true",
				"G(false)",
				"H true",
				"O(false)", "Y(false)" }){
			assertEquals(parseOld(input), parse(input));
		}
	}

	@Test
	public void testBinaryOp() throws Exception {
		for (String input : new String[] {
				"true&false",
				"true & false",
				"true or false",
				"true => false",
				"(true)U false",
				"sink W (deadlock)",
				"trueRfalse",
				"sink S (deadlock)", "trueTfalse"}){
			assertEquals(parseOld(input), parse(input));
		}
	}

	@Test
	public void testPredicate() throws Exception {
		for (String input : new String[] {
				"{unpa rsed}",
				"{abc} => true",
				"not {abc & def}",
				"{ abc [def] ghi }", "{ abc {def} ghi }"}){
			assertEquals(parseOld(input), parse(input));
		}
	}

	@Test
	public void testAction() throws Exception {
		for (String input : new String[] {
				"[unpa rsed]",
				"[abc] => true",
				"not [abc & def]",
				"[ abc [def] ghi ]", "[ abc {def} ghi ]"}){
			assertEquals(parseOld(input), parse(input));
		}
	}

	@Test
	public void testEnabled() throws Exception {
		for (String input : new String[] {
				"e(bla)",
				"e(a (b) c)",
				"e([ abc [def] ghi ])", "e( abc {def} ghi )"}){
			assertEquals(parseOld(input), parse(input));
		}
	}

	@Test
	public void testPrecedenceNotAndOr() throws Exception {
		for (String input : new String[] {
				"true",
				"not true",
				"true & false",
				"not true & false",
				"not true & not false",
				"not not true & not false",
				"true & false & true",
				"true & false or sink",
				//"true or false & true",
		"true or false or true"}){
			assertEquals(parseOld(input), parse(input));
		}
	}

	@Test
	public void testPrecedenceImplies() throws Exception {
		for (String input : new String[] {
				"true=>false",
				"not true => not false",
				"true & true => true or false",
				"true or not true => not true & false",
				"true => false => sink", "true => false => sink => deadlock"}){
			assertEquals(parseOld(input), parse(input));
		}
	}

	@Test
	public void testPrecedenceBinaryOp() throws Exception {
		for (String input : new String[] {
				//"true U false",
				//"sink R GF false",
				//"true & false W false",
				//"false W true & false"
				//"true => false & sink W sink or true",
				//"true U false R sink",
				//"true S false & sink T deadlock", "true S false or sink T deadlock "
		}){
			assertEquals(parseOld(input), parse(input));
		}
	}

}
