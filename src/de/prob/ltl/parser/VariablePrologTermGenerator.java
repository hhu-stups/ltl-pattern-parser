package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser.Var_assignContext;
import de.prob.ltl.parser.LtlParser.Var_defContext;
import de.prob.ltl.parser.LtlParser.VariableCallAtomContext;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;

public class VariablePrologTermGenerator extends LtlPrologTermGenerator {

	private Variable variable;

	public VariablePrologTermGenerator(Variable variable, SymbolTable symbolTable, IPrologTermOutput pto,
			String currentStateID, ProBParserBase specParser) {
		super(symbolTable, pto, currentStateID, specParser);
		this.variable = variable;
	}

	@Override
	public void enterVar_def(Var_defContext ctx) {

	}

	@Override
	public void enterVar_assign(Var_assignContext ctx) {

	}

	@Override
	public void enterVariableCallAtom(VariableCallAtomContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();
		Variable var = (Variable) symbolTable.resolve(name);

		if (var.equals(variable)) {
			ParseTreeWalker.DEFAULT.walk(new VariablePrologTermGenerator(var, symbolTable, pto, currentStateID, specParser), var.getOldValueContext());
		} else  {
			ParseTreeWalker.DEFAULT.walk(new VariablePrologTermGenerator(var, symbolTable, pto, currentStateID, specParser), var.getValueContext());
		}
	}

}
