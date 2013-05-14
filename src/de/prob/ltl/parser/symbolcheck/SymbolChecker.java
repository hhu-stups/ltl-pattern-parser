package de.prob.ltl.parser.symbolcheck;

import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlBaseListener;
import de.prob.ltl.parser.LtlParser.PatternCallExpressionContext;
import de.prob.ltl.parser.LtlParser.PatternVarExpressionContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.symboltable.PatternScope;
import de.prob.ltl.parser.symboltable.PatternSymbol;
import de.prob.ltl.parser.symboltable.Symbol;
import de.prob.ltl.parser.symboltable.SymbolTable;

public class SymbolChecker extends LtlBaseListener {

	private SymbolTable symbolTable;
	private List<Symbol> varSymbolsInScope;

	public SymbolChecker(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
		varSymbolsInScope = getVarSymbolsInScope();
	}

	@Override
	public void enterPattern_def(Pattern_defContext ctx) {
		if (symbolTable.scopeExists(ctx)) {
			symbolTable.setCurrentScope(ctx);
			varSymbolsInScope = getVarSymbolsInScope();
		}
	}

	@Override
	public void exitPattern_def(Pattern_defContext ctx) {
		if (varSymbolsInScope.size() > 0) {
			// Warning for unused variables
			for (Symbol symbol : varSymbolsInScope) {
				symbolTable.getParser().notifyWarningListeners("Variable not used: " + symbol.getName(), symbol);
			}
		}
		symbolTable.popScope();
	}

	@Override
	public void exitPatternVarExpression(PatternVarExpressionContext ctx) {
		TerminalNode argNode = ctx.PATTERN_ID();
		String name = argNode.getText();

		Symbol var = symbolTable.resolve(name);
		if (var == null) {
			String msg = "No such variable: " + name;
			Token token = argNode.getSymbol();
			symbolTable.getParser().notifyErrorListeners(token, msg, new RecognitionException(msg, symbolTable.getParser(), token.getInputStream(), ctx));
		}
		if (var instanceof PatternSymbol) {
			String msg = name + " is not a variable";
			Token token = argNode.getSymbol();
			symbolTable.getParser().notifyErrorListeners(token, msg, new RecognitionException(msg, symbolTable.getParser(), token.getInputStream(), ctx));
		}
		varSymbolsInScope.remove(var);
	}

	@Override
	public void exitPatternCallExpression(PatternCallExpressionContext ctx) {
		TerminalNode patternNode = ctx.PATTERN_ID();
		int args = ctx.expression().size();
		PatternScope patternScope = PatternScope.determine(ctx.pattern_scope());

		String name = patternNode.getText() + "/" + patternScope.name() + "/" + args;

		if (isRecursiveCall(name)) {
			String msg = "Recusive call detected: " + name;
			Token token = patternNode.getSymbol();
			symbolTable.getParser().notifyErrorListeners(token, msg, new RecognitionException(msg, symbolTable.getParser(), token.getInputStream(), ctx));
		}

		Symbol pattern = symbolTable.resolve(name);
		if (pattern == null) {
			String msg = "No such pattern: " + name;
			Token token = patternNode.getSymbol();
			symbolTable.getParser().notifyErrorListeners(token, msg, new RecognitionException(msg, symbolTable.getParser(), token.getInputStream(), ctx));
		}
		if (!(pattern instanceof PatternSymbol)) {
			String msg = name + " is not a pattern";
			Token token = patternNode.getSymbol();
			symbolTable.getParser().notifyErrorListeners(token, msg, new RecognitionException(msg, symbolTable.getParser(), token.getInputStream(), ctx));
		}
	}

	private boolean isRecursiveCall(String symbolId) {
		Symbol scopeSymbol = symbolTable.getCurrentScope().getScopeSymbol();
		return (scopeSymbol != null && scopeSymbol.getSymbolID().equals(symbolId));
	}

	private List<Symbol> getVarSymbolsInScope() {
		List<Symbol> symbols = symbolTable.getCurrentScope().getSymbols();
		Iterator<Symbol> it = symbols.iterator();

		while (it.hasNext()) {
			if (it.next() instanceof PatternSymbol) {
				it.remove();
			}
		}

		return symbols;
	}
}
