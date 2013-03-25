package de.prob.ltl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.prob.parserbase.ProBParserBase;


public class UnaryOpTest extends AbstractLtlParserTest {

	@Override
	public ProBParserBase getProBParserBase() {
		return null;
	}

	@Test
	public void testCombined() throws Exception {
		assertEquals("globally(finally(true))", parse("G F true"));
		assertEquals("globally(finally(true))", parse("GF true"));
		throwsRuntimeException("GFtrue");
	}

	@Test
	public void testNormal() throws Exception {
		assertEquals("finally(true)", parse("F true"));
		assertEquals("globally(true)", parse("G true"));
		throwsRuntimeException("Ftrue");
	}

}
