package de.prob.ltl.parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.LtlParser.VariableCallAtomContext;
import de.prob.ltl.parser.symboltable.Pattern;
import de.prob.ltl.parser.symboltable.Scope;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;

public class PatternPrologTermGenerator extends LtlPrologTermGenerator {

	private List<ParserRuleContext> arguments = new LinkedList<ParserRuleContext>();

	public PatternPrologTermGenerator(SymbolTable symbolTable, List<ParserRuleContext> arguments, IPrologTermOutput pto,
			String currentStateID, ProBParserBase specParser) {
		super(symbolTable, pto, currentStateID, specParser);
		this.arguments = arguments;
	}

	@Override
	public void enterPattern_def(Pattern_defContext ctx) {
		symbolTable.pushScope(ctx);
	}

	@Override
	public void exitPattern_def(Pattern_defContext ctx) {
		symbolTable.popScope();
	}

	@Override
	public void enterVariableCallAtom(VariableCallAtomContext ctx) {
		Scope scope = symbolTable.getCurrentScope();
		if (!(scope instanceof Pattern)) {return;
		// TODO throw new RuntimeException("Should be in pattern scope.");
		}
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();

		Variable var = (Variable) symbolTable.resolve(name);
		int index = ((Pattern) scope).getParameterIndex(var);
		if (index > -1) {
			ParseTreeWalker.DEFAULT.walk(new LtlPrologTermGenerator(symbolTable, pto, currentStateID, specParser), arguments.get(index));
		} else {
			super.enterVariableCallAtom(ctx);
		}
	}

}
