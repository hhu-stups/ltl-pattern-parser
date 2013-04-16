package de.prob.ltl.parser;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.LtlParser.StartContext;
import de.prob.ltl.parser.symbol.PatternSymbol;
import de.prob.ltl.parser.symbol.VariableSymbol;

public class SymbolCollector extends LtlBaseListener {

	private SymbolTable symbolTable;

	public SymbolCollector(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	@Override
	public void exitStart(StartContext ctx) {
		//symbolTable.print();
	}

	@Override
	public void enterPattern_def(Pattern_defContext ctx) {
		List<TerminalNode> ids = ctx.PATTERN_ID();
		String name = ids.remove(0).getText();

		PatternSymbol pattern = new PatternSymbol(name, ids.size());
		symbolTable.define(pattern);
		symbolTable.pushScope(ctx);

		for (TerminalNode id : ids) {
			VariableSymbol arg = new VariableSymbol(id.getText());
			symbolTable.define(arg);
		}
	}

	@Override
	public void exitPattern_def(Pattern_defContext ctx) {
		symbolTable.popScope();
	}

}
