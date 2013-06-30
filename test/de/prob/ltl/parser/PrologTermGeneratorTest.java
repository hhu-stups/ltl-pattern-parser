package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import de.prob.ltl.parser.prolog.PrologTermGenerator;
import de.prob.prolog.output.StructuredPrologOutput;

public class PrologTermGeneratorTest extends AbstractParserTest {

	@Override
	protected String parse(String input) {
		ParseTree ast = parseToTree(input);

		StructuredPrologOutput pto = new StructuredPrologOutput();
		ParseTreeWalker.DEFAULT.walk(new PrologTermGenerator(parserRuleCall.getSymbolTable(), pto, "current", parserBase), ast);
		pto.fullstop();
		return pto.getSentences().get(0).toString();
	}

	@Test
	public void testCollectPatternSimple() {
		System.out.println(parse("def pattern(x): x pattern(true)"));
		System.out.println(parse("def pattern(): true GF pattern() or false"));
		System.out.println(parse("def pattern(x): true GF pattern(true) or false"));
		System.out.println(parse("def pattern(x): true pattern(pattern(true))"));
		System.out.println(parse("def pattern(x, y): true GF pattern(true,false) "));
		System.out.println(parse("def pattern(x, y): x or y GF pattern(true,false) "));

		System.out.println(parse(" var y: true def pattern(x): y: false x pattern(y) or y"));
	}

	@Test
	public void testCollectVariableSimple() {
		System.out.println(parse("var x: true x or false"));
		System.out.println(parse("var x: true x: false x or false"));
		System.out.println(parse("var x: true x: x and false x"));
	}

	@Test
	public void testCollectLoopSimple() {
		System.out.println(parse("def pattern(): var x: false loop 1 up to 3: x: x or true end x pattern()"));
		System.out.println(parse("def pattern(n:num): var x: false loop n down to 1: x: x or true end x pattern(3)"));
	}

}
