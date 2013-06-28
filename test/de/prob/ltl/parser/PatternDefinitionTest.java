package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Before;
import org.junit.Test;

import de.prob.ltl.parser.symboltable.SymbolTable;

public class PatternDefinitionTest extends AbstractParserTest {

	protected static ParserRuleCall PATTERN_DEF_PARSER_RULE_CALL = new ParserRuleCall() {

		@Override
		public ParseTree callParserRule(LtlParser parser) {
			return parser.pattern_def();
		}

		@Override
		public SymbolTable getSymbolTable() {
			return new SymbolTable();
		}
	};

	protected static ParserRuleCall SIMPLE_PARSER_RULE_CALL = new ParserRuleCall() {

		@Override
		public ParseTree callParserRule(LtlParser parser) {
			return parser.start();
		}

		@Override
		public SymbolTable getSymbolTable() {
			return new SymbolTable();
		}
	};

	@Before
	public void setupTest() {
		parserRuleCall = PATTERN_DEF_PARSER_RULE_CALL;
	}

	@Test
	public void testDefinitionSimple() throws Exception {
		parse("def pattern(): true or false");
		parse("def f(x): x");
		parse("def A(x): x");
		parse("def fAa_0b(x): x");
	}

	@Test
	public void testDefinitionParamsSimple() throws Exception {
		parse("def pattern(x): x or false");
		parse("def pattern(x, y): x or y");
		parse("def pattern(x, y, z): x or y or z");
	}

	@Test
	public void testDefinitionScopeSimple() throws Exception {
		parse("def <global> pattern(): false");
		parse("def <before r> pattern(): false");
		parse("def <after q> pattern(): false");
		parse("def <between q and r> pattern(): false");
		parse("def <between q & r> pattern(): false");
		parse("def <after q until r> pattern(): false");
	}

	@Test
	public void testDefinitionNumParamSimple() throws Exception {
		parse("def pattern(n:num): false");
		parse("def pattern(x, n:num): false");
		parse("def pattern(n:num, x): false");
	}

	@Test
	public void testCallSimple() throws Exception {
		parserRuleCall = SIMPLE_PARSER_RULE_CALL;

		parse("pattern()");
	}

	@Test
	public void testCallInDefSimple() throws Exception {
		parse("def pattern(): other()");
	}

	@Test
	public void testCallArgsSimple() throws Exception {
		parserRuleCall = SIMPLE_PARSER_RULE_CALL;

		parse("pattern(true, false)");
		parse("pattern(true, false, true or false)");
	}

	@Test
	public void testCallNumArgsSimple() throws Exception {
		parserRuleCall = SIMPLE_PARSER_RULE_CALL;

		parse("pattern(true, 1)");
		parse("pattern(false, 1234567890, true)");

		throwsException("1 or false");
		throwsException("pattern(true, -1)");
	}

	@Test
	public void testCallScopeSimple() throws Exception {
		parserRuleCall = SIMPLE_PARSER_RULE_CALL;

		parse("pattern<global>()");
		parse("pattern<before true>()");
		parse("pattern<after true>()");
		parse("pattern<between true and false>()");
		parse("pattern<between true & false>()");
		parse("pattern<after true until false>()");
	}

	@Test
	public void testCallScopeArgs() throws Exception {
		parserRuleCall = SIMPLE_PARSER_RULE_CALL;

		parse("pattern<before x>()");
		parse("pattern<after other()>()");
		parse("pattern<between (true => (GF deadlock) and false) and false>()");
		parse("pattern<between {...} & e(...)>()");
		parse("pattern<after [...] until current>()");
		parse("pattern<before (!false)>()");
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
		throwsException("def 1fAa_0b(x): x 1fAa_0b(true)");
		throwsException("def a b(x): x a b(true)");
		throwsException("def X(x): x X(true)");
		throwsException("def F(x): x F(true)");
		throwsException("def U(x): x U(true)");
		throwsException("def W(x): x W(true)");
		throwsException("def sink(x): x sink(true)");
		throwsException("def deadlock(x): x deadlock(true)");
		throwsException("def and(x): x and(true)");
		throwsException("def global(x): x global(true)");

		throwsException("def f(a,a): a f(false,true)");

		throwsException("def");
		throwsException("def ");
		throwsException("def abc");
		throwsException("def abc()");
		throwsException("def abc():");
		throwsException("def abc(a):");
	}

}
