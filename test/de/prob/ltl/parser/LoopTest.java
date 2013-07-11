package de.prob.ltl.parser;

import junit.framework.Assert;

import org.junit.Test;

public class LoopTest extends AbstractParserTest {

	// Helper
	protected void parseLoop(String input) {
		LtlParser parser = createParser(input);

		parser.loop();

		if (hasErrors(parser)) {
			throw new RuntimeException();
		}
	}

	protected void throwsExceptionLoop(String input) {
		try {
			parseLoop(input);
			Assert.fail("Exception should have been thrown.");
		} catch (RuntimeException e) {
		}
	}

	// Tests
	@Test
	public void testDefinitionSimple() throws Exception {
		parseLoop("loop 1 up to 2: var s: true end");
		parseLoop("loop 2 down to 1: var s: true end");
		parseLoop("loop s up to e: var x: true end");
		parseLoop("loop e up to s: var x: true end");
		parseLoop("loop e up to s: x: true end");
		parseLoop("loop e up to s: var x: false x: true end");

		throwsExceptionLoop("loop true up to 1: var x: true end");
		throwsExceptionLoop("loop 1 up to true: var x: true end");
		throwsExceptionLoop("loop 1 up to 2: true end");
		throwsExceptionLoop("loop 1 up to 2: end");
		throwsExceptionLoop("loop 1 up to 2: def pattern(): true var x:true end");
	}

	@Test
	public void testDefinitionInPatternDef() throws Exception {
		parse("def pattern(): loop 1 up to 2: var x: true end true pattern()");
		parse("def pattern(x): loop 1 up to 2: x: x or true end x pattern(false)");
		parse("loop 1 up to 2: var x: true end true");
	}

}
