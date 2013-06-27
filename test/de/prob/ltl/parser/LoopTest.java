package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Before;
import org.junit.Test;

import de.prob.ltl.parser.symboltable.SymbolTable;

public class LoopTest extends AbstractParserTest {

	protected static ParserRuleCall LOOP_PARSER_RULE_CALL = new ParserRuleCall() {

		@Override
		public ParseTree callParserRule(LtlParser parser) {
			return parser.loop();
		}

		@Override
		public SymbolTable getSymbolTable() {
			return null;
		}
	};

	@Before
	public void setupTest() {
		parserRuleCall = DEFAULT_PARSER_RULE_CALL;
	}

	@Test
	public void testDefinitionSimple() throws Exception {
		parserRuleCall = LOOP_PARSER_RULE_CALL;
		parse("loop 1 up to 2: var s: true end");
		parse("loop 2 down to 1: var s: true end");
		parse("loop s up to e: var x: true end");
		parse("loop e up to s: var x: true end");
		parse("loop e up to s: x: true end");
		parse("loop e up to s: var x: false x: true end");

		throwsException("loop true up to 1: var x: true end");
		throwsException("loop 1 up to true: var x: true end");
		throwsException("loop 1 up to 2: true end");
		throwsException("loop 1 up to 2: end");
		throwsException("loop 1 up to 2: def pattern(): true var x:true end");
	}

	@Test
	public void testDefinitionInPatternDef() throws Exception {
		parse("def pattern(): loop 1 up to 2: var x: true end true pattern()");
		parse("def pattern(x): loop 1 up to 2: x: x or true end x pattern(false)");
		throwsException("loop 1 up to 2: var x: true end true");
	}

}
