package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.BeforeClass;
import org.junit.Test;

import de.prob.ltl.parser.symboltable.SymbolTable;

public class SematicTest extends AbstractParserTest {

	protected static ParserRuleCall SEMANTIC_CHECK_PARSER_RULE_CALL = new ParserRuleCall() {

		@Override
		public ParseTree callParserRule(LtlParser parser) {
			ParseTree result = parser.start();

			ParseTreeWalker walker = new ParseTreeWalker();
			SymbolTable symbolTable = new SymbolTable();
			walker.walk(new SematicCheckPhase1(symbolTable), result);
			walker.walk(new SematicCheckPhase2(symbolTable), result);

			System.out.println(symbolTable.getSymbols());

			return result;
		}
	};

	@BeforeClass
	public static void setupTests() {
		parserRuleCall = SEMANTIC_CHECK_PARSER_RULE_CALL;
	}

	@Test
	public void testDefinitionPatternScopes() throws Exception {
		String pattern1 = "def pattern(): true ";
		String pattern2 = "def <global> pattern(): true ";
		String pattern3 = "def <before r> pattern(): r ";
		String pattern4 = "def <after q> pattern(): true ";
		String pattern5 = "def <between q and r> pattern(): true ";
		String pattern6 = "def <after q until r> pattern(): true ";
		String pattern7 = "def <global> pattern(x): true ";
		String expr = "true ";
		throwsException(pattern1 +  pattern2 + expr);
		parse(pattern1 +  pattern7 + expr);
		parse(pattern1 + pattern3 + expr + pattern4 + pattern5 + pattern6);
	}

	@Test
	public void testDefinitionPatternParameter() throws Exception {
		String pattern1 = "def pattern(x, y): true ";
		String pattern2 = "def pattern2(x, y): true ";
		String expr = "true ";
		parse(pattern1 +  pattern2 + expr);
	}

	@Test
	public void testDefinitionPatternError() throws Exception {
		String pattern1 = "def pattern(x, x): true ";
		String pattern2 = "def pattern(x, y): z ";
		String pattern3 = "def <before r> pattern(r): true ";
		String expr = "true ";
		throwsException(pattern1 + expr);
		throwsException(pattern2 + expr);
		throwsException(pattern3 + expr);
	}

	@Test
	public void testCallPattern() throws Exception {
		String pattern1 = "def pattern(): true ";
		String pattern2 = "def pattern2(): pattern() ";
		String expr = "pattern2() ";
		parse(pattern1 +  pattern2 + expr);
		parse(pattern2 +  pattern1 + expr);
		parse(expr + pattern2 +  pattern1);
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
		String pattern3 = "def <before x> pattern(): true ";
		String pattern4 = "def pattern(): x ";
		String pattern5 = "def pattern(): var x: true true ";

		String expr = "true ";

		String var1 = "var x: true ";
		parse(var1 + pattern1 + expr);
		parse(pattern1 + var1 + expr);

		throwsException(var1 + pattern2 + expr);
		parse(pattern2 + var1 + expr);

		throwsException(var1 + pattern3 + expr);
		parse(pattern3 + var1 + expr);

		parse(var1 + pattern4 + expr);
		throwsException(pattern4 + var1 + expr);

		throwsException(var1 + pattern5 + expr);
		parse(pattern5 + var1 + expr);

		String var2 = "var x: pattern() ";
		String var3 = "var x: pattern(true) ";
		String var4 = "var x: pattern<before true>() ";
		parse(var2 + pattern1 + expr);
		parse(pattern1 + var2 + expr);

		throwsException(var3 + pattern2 + expr);
		parse(pattern2 + var3 + expr);

		throwsException(var4 + pattern3 + expr);
		parse(pattern3 + var4 + expr);

		// TODO cycle detection throwsException(var2 + pattern4 + expr);
		throwsException(pattern4 + var2 + expr);

		throwsException(var2 + pattern5 + expr);
		parse(pattern5 + var2 + expr);
	}

	/*@Test
	public void testRecursiveDefinitionCall() throws Exception {
		throwsException("def f(a): f(a) or false f(true)");
	}

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
