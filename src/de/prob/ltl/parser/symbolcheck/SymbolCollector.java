package de.prob.ltl.parser.symbolcheck;

import java.util.List;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlBaseListener;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.symboltable.PatternSymbol;
import de.prob.ltl.parser.symboltable.Symbol;
import de.prob.ltl.parser.symboltable.SymbolTable;

public class SymbolCollector extends LtlBaseListener {

	private SymbolTable symbolTable;

	public SymbolCollector(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	@Override
	public void enterPattern_def(Pattern_defContext ctx) {
		if (ctx.exception != null || hasErrorNode(ctx.PATTERN_ID())) {
			// Exception in pattern definition, so do not collect pattern
			return;
		}
		List<TerminalNode> ids = ctx.PATTERN_ID();
		TerminalNode nameNode = ids.remove(0);
		String name = nameNode.getText();

		PatternSymbol pattern = new PatternSymbol(name, nameNode.getSymbol(), ids.size());
		symbolTable.define(pattern);
		symbolTable.pushScope(ctx, pattern);

		for (TerminalNode id : ids) {
			Symbol arg = new Symbol(id.getText(), id.getSymbol());
			symbolTable.define(arg);
		}
	}

	@Override
	public void exitPattern_def(Pattern_defContext ctx) {
		symbolTable.popScope();
	}

	private boolean hasErrorNode(List<TerminalNode> nodes) {
		for (TerminalNode node : nodes) {
			if (node instanceof ErrorNode) {
				return true;
			}
		}
		return false;
	}

}
