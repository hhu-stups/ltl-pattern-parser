package de.prob.ltl.parser.pattern;

import junit.framework.Assert;

import org.junit.Test;

import de.prob.ltl.parser.AbstractParserTest;

public class PatternManagerTest extends AbstractParserTest {

	private Pattern createPattern(String code, TestErrorListener errorListener, TestWarningListener warningListener) {
		Pattern pattern = new Pattern();
		pattern.addErrorListener(errorListener);
		pattern.addWarningListener(warningListener);
		pattern.setCode(code);

		return pattern;
	}

	@Test
	public void testAddPattern() throws Exception {
		PatternManager patternManager = new PatternManager();
		TestErrorListener errorListener = new TestErrorListener();
		TestWarningListener warningListener = new TestWarningListener();

		Pattern pattern = createPattern("def pattern(): true", errorListener, warningListener);
		patternManager.addPattern(pattern);

		Assert.assertEquals(0, errorListener.getErrors());
		Assert.assertEquals(0, warningListener.getCount());

		Pattern pattern2 = createPattern("def pattern(): false", errorListener, warningListener);
		patternManager.addPattern(pattern2);

		Assert.assertEquals(1, errorListener.getErrors());
		Assert.assertEquals(0, warningListener.getCount());

		parse("pattern()", patternManager);
	}

	@Test
	public void testRemovePattern() throws Exception {
		PatternManager patternManager = new PatternManager();
		TestErrorListener errorListener = new TestErrorListener();
		TestWarningListener warningListener = new TestWarningListener();

		Pattern pattern = createPattern("def pattern(): true", errorListener, warningListener);
		patternManager.addPattern(pattern);

		Assert.assertEquals(0, errorListener.getErrors());
		Assert.assertEquals(0, warningListener.getCount());

		patternManager.removePattern(pattern);

		Pattern pattern2 = createPattern("def pattern(): false", errorListener, warningListener);
		patternManager.addPattern(pattern2);

		Assert.assertEquals(0, errorListener.getErrors());
		Assert.assertEquals(0, warningListener.getCount());

		parse("pattern()", patternManager);
	}

	@Test
	public void testRemovePattern2() throws Exception {
		PatternManager patternManager = new PatternManager();
		TestErrorListener errorListener = new TestErrorListener();
		TestWarningListener warningListener = new TestWarningListener();

		Pattern pattern = createPattern("def pattern(): true", errorListener, warningListener);
		patternManager.addPattern(pattern);

		Assert.assertEquals(0, errorListener.getErrors());
		Assert.assertEquals(0, warningListener.getCount());

		parse("pattern()", patternManager);

		patternManager.removePattern(pattern);

		throwsException("pattern()", patternManager);
	}

	@Test
	public void testWarnings() throws Exception {
		PatternManager patternManager = new PatternManager();
		TestErrorListener errorListener = new TestErrorListener();
		TestWarningListener warningListener = new TestWarningListener();

		Pattern pattern = createPattern("def pattern(x): true", errorListener, warningListener);
		patternManager.addPattern(pattern);

		Assert.assertEquals(0, errorListener.getErrors());
		Assert.assertEquals(1, warningListener.getCount());

		parse("pattern(true)", patternManager);

		patternManager.removePattern(pattern);
		patternManager.addPattern(pattern);

		Assert.assertEquals(0, errorListener.getErrors());
		Assert.assertEquals(1, warningListener.getCount());

		parse("pattern(true)", patternManager);

		patternManager.removePattern(pattern);
		pattern.setCode(pattern.getCode());
		patternManager.addPattern(pattern);

		Assert.assertEquals(0, errorListener.getErrors());
		Assert.assertEquals(2, warningListener.getCount());

		parse("pattern(true)", patternManager);

		patternManager.removePattern(pattern);
		pattern.setCode(null);
		patternManager.addPattern(pattern);

		Assert.assertEquals(0, errorListener.getErrors());
		Assert.assertEquals(2, warningListener.getCount());

		throwsException("pattern(true)", patternManager);
	}



}
