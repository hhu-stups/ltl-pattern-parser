package de.prob.ltl.parser.symboltable;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.prob.ltl.parser.symboltable.Pattern.PatternScopes;
import de.prob.ltl.parser.symboltable.Variable.VariableTypes;

public class SymbolTableTest {

	@Test
	public void testDefinePatternSimple() {
		SymbolTable st = new SymbolTable();

		List<Symbol> symbols = st.getSymbols();
		Assert.assertEquals(0, symbols.size());

		Pattern pattern1 = new Pattern(st.getCurrentScope(), "pattern_1");
		st.define(pattern1);

		symbols = st.getSymbols();
		Assert.assertEquals(1, symbols.size());

		Symbol resolvedPattern = st.resolve("pattern_1/global/0");
		Assert.assertEquals(pattern1, resolvedPattern);
	}

	@Test
	public void testDefinePatternDuplicate() {
		SymbolTable st = new SymbolTable();

		Pattern pattern1 = new Pattern(st.getCurrentScope(), "pattern_1");
		st.define(pattern1);

		Pattern pattern2 = new Pattern(st.getCurrentScope(), "pattern_1");
		try {
			st.define(pattern2);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException e) {
		}

		Symbol resolvedPattern = st.resolve("pattern_1/global/0");
		Assert.assertEquals(pattern1, resolvedPattern);
	}

	@Test
	public void testDefineVariableSimple() {
		SymbolTable st = new SymbolTable();

		List<Symbol> symbols = st.getSymbols();
		Assert.assertEquals(0, symbols.size());

		Variable var1 = new Variable("var_1", VariableTypes.var);
		st.define(var1);

		symbols = st.getSymbols();
		Assert.assertEquals(1, symbols.size());

		Symbol resolvedVar = st.resolve("var_1");
		Assert.assertEquals(var1, resolvedVar);
	}

	@Test
	public void testDefineVariableTypes() {
		SymbolTable st = new SymbolTable();

		Variable var1 = new Variable("x", VariableTypes.var);
		st.define(var1);

		Variable var2 = new Variable("x", VariableTypes.num);
		try {
			st.define(var2);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException e) {
		}

		Assert.assertEquals(var1, st.resolve("x"));
	}

	@Test
	public void testDefineVariableDuplicate() {
		SymbolTable st = new SymbolTable();

		List<Symbol> symbols = st.getSymbols();
		Assert.assertEquals(0, symbols.size());

		Variable var1 = new Variable("var_1", VariableTypes.var);
		st.define(var1);

		Variable var2 = new Variable("var_1", VariableTypes.num);
		try {
			st.define(var2);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException e) {
		}

		Symbol resolvedVar = st.resolve("var_1");
		Assert.assertEquals(var1, resolvedVar);
	}

	@Test
	public void testPushPopScope() {
		SymbolTable st = new SymbolTable();

		Pattern pattern = new Pattern(st.getCurrentScope(), "pattern");
		st.define(pattern);
		st.pushScope(pattern, null);

		Variable var = new Variable("var1", VariableTypes.var);
		st.define(var);

		Assert.assertEquals(var, st.resolve("var1"));
		Assert.assertEquals(pattern, st.resolve("pattern/global/0"));

		st.popScope();

		Assert.assertNull(st.resolve("var1"));
		Assert.assertEquals(pattern, st.resolve("pattern/global/0"));
	}

	@Test
	public void testPushPopScope2() {
		SymbolTable st = new SymbolTable();

		Pattern pattern = new Pattern(st.getCurrentScope(), "pattern");
		st.define(pattern);
		st.pushScope(pattern, null);
		st.pushScope(pattern, null);

		Variable var = new Variable("var1", VariableTypes.var);
		st.define(var);

		Assert.assertEquals(var, st.resolve("var1"));
		Assert.assertEquals(pattern, st.resolve("pattern/global/0"));

		st.popScope();

		Assert.assertNull(st.resolve("var1"));
		Assert.assertEquals(pattern, st.resolve("pattern/global/0"));

		st.popScope();

		Assert.assertNull(st.resolve("var1"));
		Assert.assertEquals(pattern, st.resolve("pattern/global/0"));
	}

	@Test
	public void testDefinePatternInPatternScopeError() {
		SymbolTable st = new SymbolTable();

		Pattern pattern = new Pattern(st.getCurrentScope(), "pattern");
		st.define(pattern);
		st.pushScope(pattern, null);

		Pattern pattern2 = new Pattern(st.getCurrentScope(), "pattern2");
		try {
			st.define(pattern2);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException e) {
		}

		Assert.assertNull(st.resolve("pattern2/global/0"));
		Assert.assertEquals(pattern, st.resolve("pattern/global/0"));

		st.popScope();

		Assert.assertNull(st.resolve("pattern2/global/0"));
		Assert.assertEquals(pattern, st.resolve("pattern/global/0"));
	}

	@Test
	public void testDuplicateInDifferentScopesError() {
		SymbolTable st = new SymbolTable();

		Variable var1 = new Variable("var1", VariableTypes.var);
		st.define(var1);

		Pattern pattern = new Pattern(st.getCurrentScope(), "pattern");
		st.define(pattern);
		st.pushScope(pattern, null);

		Variable var2 = new Variable("var1", VariableTypes.var);
		try {
			st.define(var2);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException e) {
		}

		Assert.assertEquals(var1, st.resolve("var1"));

		st.popScope();

		Assert.assertEquals(var1, st.resolve("var1"));
	}

	@Test
	public void testSameSymbolInDifferentPatternScopes() {
		SymbolTable st = new SymbolTable();

		Pattern pattern1 = new Pattern(st.getCurrentScope(), "pattern1");
		st.define(pattern1);
		st.pushScope(pattern1, null);

		Variable var1 = new Variable("var", VariableTypes.var);
		st.define(var1);

		Assert.assertEquals(var1, st.resolve("var"));

		st.popScope();

		Assert.assertNull(st.resolve("var1"));

		Pattern pattern2 = new Pattern(st.getCurrentScope(), "pattern2");
		st.define(pattern2);
		st.pushScope(pattern2, null);

		Variable var2 = new Variable("var", VariableTypes.var);
		st.define(var2);

		Assert.assertEquals(var2, st.resolve("var"));

		st.popScope();

		Assert.assertNull(st.resolve("var1"));
	}

	@Test
	public void testAddPatternParameter() {
		SymbolTable st = new SymbolTable();

		Pattern pattern = new Pattern(st.getCurrentScope(), "pattern");
		st.pushScope(pattern, null);

		Variable var1 = new Variable("x", VariableTypes.var);
		st.define(var1);
		pattern.addParameter(var1);

		Variable var2 = new Variable("y", VariableTypes.var);
		st.define(var2);
		pattern.addParameter(var2);

		Variable var3 = new Variable("z", VariableTypes.var);
		st.define(var3);

		Assert.assertEquals(2, pattern.getParameters().size());
		Assert.assertEquals(3, st.getSymbols().size());
		Assert.assertEquals(var1, st.resolve("x"));
		Assert.assertEquals(var2, st.resolve("y"));
		Assert.assertEquals(var3, st.resolve("z"));

		st.popScope();

		Assert.assertEquals(0, st.getSymbols().size());
		Assert.assertNull(st.resolve("x"));
		Assert.assertNull(st.resolve("y"));
		Assert.assertNull(st.resolve("z"));

		st.define(pattern);

		Assert.assertEquals(pattern, st.resolve("pattern/global/2"));
	}

	@Test
	public void testAddPatternParameterDuplicate() {
		SymbolTable st = new SymbolTable();

		Variable var1 = new Variable("x", VariableTypes.var);
		st.define(var1);

		Pattern pattern = new Pattern(st.getCurrentScope(), "pattern");
		st.pushScope(pattern, null);

		Variable var2 = new Variable("y", VariableTypes.var);
		st.define(var2);
		pattern.addParameter(var2);

		try {
			Variable var = new Variable("y", VariableTypes.var);
			st.define(var);
			pattern.addParameter(var);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException e) {
		}

		Variable var3 = new Variable("z", VariableTypes.var);
		st.define(var3);

		try {
			Variable var = new Variable("x", VariableTypes.var);
			st.define(var);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException e) {
		}

		try {
			Variable var = new Variable("z", VariableTypes.var);
			st.define(var);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException e) {
		}

		Assert.assertEquals(1, pattern.getParameters().size());
		Assert.assertEquals(3, st.getSymbols().size());
		Assert.assertEquals(var1, st.resolve("x"));
		Assert.assertEquals(var2, st.resolve("y"));
		Assert.assertEquals(var3, st.resolve("z"));

		st.popScope();

		Assert.assertEquals(1, st.getSymbols().size());
		Assert.assertEquals(var1, st.resolve("x"));
		Assert.assertNull(st.resolve("y"));
		Assert.assertNull(st.resolve("z"));

		st.define(pattern);

		Assert.assertEquals(pattern, st.resolve("pattern/global/1"));
	}

	@Test
	public void testPatternScopes() {
		SymbolTable st = new SymbolTable();

		Pattern pattern = new Pattern(st.getCurrentScope(), "pattern");
		pattern.setScope(PatternScopes.global);
		st.define(pattern);

		pattern = new Pattern(st.getCurrentScope(), "pattern");
		pattern.setScope(PatternScopes.before);
		st.define(pattern);

		pattern = new Pattern(st.getCurrentScope(), "pattern");
		pattern.setScope(PatternScopes.after);
		st.define(pattern);

		pattern = new Pattern(st.getCurrentScope(), "pattern");
		pattern.setScope(PatternScopes.between);
		st.define(pattern);

		pattern = new Pattern(st.getCurrentScope(), "pattern");
		pattern.setScope(PatternScopes.after_until);
		st.define(pattern);

		pattern = new Pattern(st.getCurrentScope(), "pattern");
		try {
			st.define(pattern);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException ex) {

		}

		pattern = new Pattern(st.getCurrentScope(), "pattern");
		pattern.setScope(PatternScopes.global);
		pattern.addParameter(new Variable("x", VariableTypes.var));
		st.define(pattern);

		pattern = new Pattern(st.getCurrentScope(), "pattern");
		pattern.setScope(PatternScopes.global);
		pattern.addParameter(new Variable("n", VariableTypes.num));
		try {
			st.define(pattern);
			Assert.fail("Exception should have been thrown");
		} catch(RuntimeException ex) {

		}

		Assert.assertNotNull(st.resolve("pattern/global/0"));
		Assert.assertNotNull(st.resolve("pattern/before/0"));
		Assert.assertNotNull(st.resolve("pattern/after/0"));
		Assert.assertNotNull(st.resolve("pattern/between/0"));
		Assert.assertNotNull(st.resolve("pattern/after_until/0"));
		Assert.assertNotNull(st.resolve("pattern/global/1"));
	}

	@Test
	public void testCheckTypes() {
		SymbolTable st = new SymbolTable();

		Pattern pattern = new Pattern(st.getCurrentScope(), "pattern");
		st.pushScope(pattern, null);

		Variable var1 = new Variable("x", VariableTypes.num);
		st.define(var1);
		pattern.addParameter(var1);

		Variable var2 = new Variable("y", VariableTypes.var);
		st.define(var2);
		pattern.addParameter(var2);
		st.popScope();
		st.define(pattern);

		Assert.assertEquals(pattern, st.resolve("pattern/global/2"));

		Pattern call1 = new Pattern(st.getCurrentScope(), "pattern");
		call1.addParameter(new Variable(null, VariableTypes.num));
		call1.addParameter(new Variable(null, VariableTypes.var));

		Assert.assertTrue(st.checkTypes(call1));

		Pattern call2 = new Pattern(st.getCurrentScope(), "pattern");
		call2.addParameter(new Variable(null, VariableTypes.num));
		call2.addParameter(new Variable(null, VariableTypes.num));

		Assert.assertFalse(st.checkTypes(call2));
	}

}
