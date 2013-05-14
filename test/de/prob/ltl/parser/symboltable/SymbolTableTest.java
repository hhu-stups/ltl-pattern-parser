package de.prob.ltl.parser.symboltable;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.junit.Assert;
import org.junit.Test;

import de.prob.ltl.TestErrorListener;
import de.prob.ltl.parser.LtlParser;

public class SymbolTableTest {

	@Test
	public void testPushPopScope() {
		SymbolTable table = new SymbolTable(null);
		DummyParseTree dummy1 = new DummyParseTree("Dummy 1");
		DummyParseTree dummy2 = new DummyParseTree("Dummy 2");
		Assert.assertNotSame(dummy1, dummy2);

		Assert.assertEquals(table.getGlobalScope(), table.getCurrentScope());
		Assert.assertNotNull(table.getGlobalScope());

		// Push
		table.pushScope(dummy1, null);
		Scope scope1 = table.getCurrentScope();
		Assert.assertNotSame(table.getGlobalScope(), scope1);

		table.pushScope(dummy2, null);
		Scope scope2 = table.getCurrentScope();
		Assert.assertNotSame(table.getGlobalScope(), scope1);
		Assert.assertNotSame(table.getGlobalScope(), scope2);

		Assert.assertNotSame(scope1, scope2);

		// Pop
		table.popScope();
		Assert.assertEquals(scope1, table.getCurrentScope());

		table.popScope();
		Assert.assertEquals(table.getGlobalScope(), table.getCurrentScope());

		// Pop global
		table.popScope();
		Assert.assertEquals(table.getGlobalScope(), table.getCurrentScope());
		Assert.assertNotNull(table.getGlobalScope());

		// Push same scope again
		table.pushScope(dummy2, null);
		Assert.assertEquals(scope2, table.getCurrentScope());
	}

	@Test
	public void testSetScope() {
		SymbolTable table = new SymbolTable(null);
		DummyParseTree dummy1 = new DummyParseTree("Dummy 1");
		DummyParseTree dummy2 = new DummyParseTree("Dummy 2");

		table.pushScope(dummy1, null);
		Scope scope1 = table.getCurrentScope();
		table.pushScope(dummy2, null);
		Scope scope2 = table.getCurrentScope();

		Assert.assertEquals(scope2, table.getCurrentScope());

		// Set scope by parsetree
		table.setCurrentScope(dummy1);
		Assert.assertEquals(scope1, table.getCurrentScope());

		table.setCurrentScope(dummy2);
		Assert.assertEquals(scope2, table.getCurrentScope());

		// Set global scope
		table.setCurrentScope(null);
		Assert.assertEquals(table.getGlobalScope(), table.getCurrentScope());
	}

	@Test
	public void testDefineResolve() {
		LtlParser parser = new LtlParser(null);
		TestErrorListener listener = new TestErrorListener();
		parser.removeErrorListeners();
		parser.addErrorListener(listener);

		SymbolTable table = new SymbolTable(parser);
		DummyParseTree dummy1 = new DummyParseTree("Dummy 1");
		DummyParseTree dummy2 = new DummyParseTree("Dummy 2");
		Symbol symbolA = new Symbol("a", new DummyToken());
		Symbol symbolB = new Symbol("b", null);
		Symbol symbolC = new Symbol("c", null);
		PatternSymbol patternF = new PatternSymbol("f", null, 2);
		PatternSymbol patternG = new PatternSymbol("g", null, 1);
		PatternSymbol patternH = new PatternSymbol("h", null, 3);

		// Define in global
		Assert.assertEquals(0, listener.getErrors());
		table.define(symbolA);
		Assert.assertEquals(1, listener.getErrors());
		table.define(patternF);

		// Define in scope1
		table.pushScope(dummy1, null);
		table.define(symbolB);
		table.define(patternG);

		// Define in scope2
		table.pushScope(dummy2, null);
		table.define(symbolC);
		table.define(patternH);

		// Resolve
		Assert.assertEquals(symbolC, table.resolve("c"));
		Assert.assertEquals(patternH, table.resolve("h/3"));
		Assert.assertEquals(symbolB, table.resolve("b"));
		Assert.assertEquals(patternG, table.resolve("g/1"));
		Assert.assertNull(table.resolve("a"));
		Assert.assertEquals(patternF, table.resolve("f/2"));

		// Redefine
		Symbol symbolA2 = new Symbol("a", null);
		PatternSymbol patternF2 = new PatternSymbol("f", null, 2);
		PatternSymbol patternG2 = new PatternSymbol("g", null, 2);
		PatternSymbol patternH2 = new PatternSymbol("h", null, 3);
		table.define(symbolA2);
		table.define(patternF2);
		table.define(patternG2);
		table.define(patternH2);

		Assert.assertEquals(symbolA2, table.resolve("a"));
		Assert.assertEquals(patternG, table.resolve("g/1"));
		Assert.assertEquals(patternG2, table.resolve("g/2"));
		Assert.assertEquals(patternH2, table.resolve("h/3"));

		Assert.assertEquals(patternF2, table.resolve("f/2"));
		table.popScope();
		Assert.assertEquals(patternF, table.resolve("f/2"));

		// Resolve non existing symbol
		Assert.assertNull(table.resolve("test"));
		Assert.assertNull(table.resolve("f/1"));
		Assert.assertEquals(1, listener.getErrors());
	}

	// Helper
	class DummyParseTree implements ParseTree {

		private String name;

		public DummyParseTree(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			DummyParseTree other = (DummyParseTree) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			return true;
		}

		@Override
		@NotNull
		public Interval getSourceInterval() {
			return new Interval(0, 0);
		}

		@Override
		public Object getPayload() {
			return null;
		}

		@Override
		public int getChildCount() {
			return 0;
		}

		@Override
		public String toStringTree() {
			return null;
		}

		@Override
		public ParseTree getParent() {
			return null;
		}

		@Override
		public ParseTree getChild(int i) {
			return null;
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			return null;
		}

		@Override
		public String getText() {
			return null;
		}

		@Override
		public String toStringTree(Parser parser) {
			return null;
		}

		private SymbolTableTest getOuterType() {
			return SymbolTableTest.this;
		}

	}

	@SuppressWarnings("serial")
	class DummyToken extends CommonToken {

		public DummyToken() {
			super(0);
		}

		@Override
		public CharStream getInputStream() {
			return null;
		}

	}

}
