package de.prob.ltl.parser.symbolcheck;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlBaseListener;
import de.prob.ltl.parser.LtlParser.PatternCallExpressionContext;
import de.prob.ltl.parser.LtlParser.PatternVarExpressionContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.symboltable.PatternSymbol;
import de.prob.ltl.parser.symboltable.Symbol;
import de.prob.ltl.parser.symboltable.SymbolTable;

public class SymbolChecker extends LtlBaseListener {

	private SymbolTable symbolTable;

	public SymbolChecker(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	@Override
	public void enterPattern_def(Pattern_defContext ctx) {
		symbolTable.setCurrentScope(ctx);
	}

	@Override
	public void exitPattern_def(Pattern_defContext ctx) {
		symbolTable.popScope();
	}

	@Override
	public void exitPatternVarExpression(PatternVarExpressionContext ctx) {
		TerminalNode argNode = ctx.PATTERN_ID();
		String name = argNode.getText();

		Symbol var = symbolTable.resolve(name);
		if (var == null) {
			error(argNode.getSymbol(), "no such variable: " + name);
		}
		if (var instanceof PatternSymbol) {
			error(argNode.getSymbol(), name + " is not a variable");
		}
	}

	@Override
	public void exitPatternCallExpression(PatternCallExpressionContext ctx) {
		TerminalNode patternNode = ctx.PATTERN_ID();
		int args = ctx.expression().size();
		String name = patternNode.getText() + "/" + args;

		if (isRecursiveCall(name)) {
			error(patternNode.getSymbol(), "Recusive call detected: " + name);
		}

		Symbol pattern = symbolTable.resolve(name);
		if (pattern == null) {
			error(patternNode.getSymbol(), "no such pattern: " + name);
		}
		if (!(pattern instanceof PatternSymbol)) {
			error(patternNode.getSymbol(), name + " is not a pattern");
		}
	}

	private boolean isRecursiveCall(String symbolId) {
		Symbol scopeSymbol = symbolTable.getCurrentScope().getScopeSymbol();
		return (scopeSymbol != null && scopeSymbol.getSymbolID().equals(symbolId));
	}

	private void error(Token t, String msg) {
		String output = String.format("line %d:%d %s\n", t.getLine(), t.getCharPositionInLine(), msg);
		throw new RuntimeException(output);
	}

}
