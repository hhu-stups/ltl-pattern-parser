package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser.AfterScopeDefContext;
import de.prob.ltl.parser.LtlParser.AfterUntilScopeDefContext;
import de.prob.ltl.parser.LtlParser.BeforeScopeDefContext;
import de.prob.ltl.parser.LtlParser.BetweenScopeDefContext;
import de.prob.ltl.parser.LtlParser.GlobalScopeDefContext;
import de.prob.ltl.parser.LtlParser.LoopContext;
import de.prob.ltl.parser.LtlParser.LoopNumArgContext;
import de.prob.ltl.parser.LtlParser.LoopVarCallArgContext;
import de.prob.ltl.parser.LtlParser.NumVarParamContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.LtlParser.VarParamContext;
import de.prob.ltl.parser.LtlParser.Var_assignContext;
import de.prob.ltl.parser.LtlParser.Var_defContext;
import de.prob.ltl.parser.LtlParser.VariableCallAtomContext;
import de.prob.ltl.parser.symboltable.Loop;
import de.prob.ltl.parser.symboltable.Loop.LoopTypes;
import de.prob.ltl.parser.symboltable.Pattern;
import de.prob.ltl.parser.symboltable.Pattern.PatternScopes;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.ltl.parser.symboltable.Variable.VariableTypes;

public class SematicCheckPhase1 extends LtlBaseListener {

	private SymbolTable symbolTable;
	private Pattern currentPattern;
	private Loop currentLoop;

	public SematicCheckPhase1(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	@Override
	public void enterLoop(LoopContext ctx) {
		LoopTypes type = (ctx.UP() != null ? LoopTypes.up : LoopTypes.down);
		currentLoop = new Loop(symbolTable.getCurrentScope(), type);
		symbolTable.pushScope(currentLoop, ctx);
	}

	@Override
	public void exitLoop(LoopContext ctx) {
		symbolTable.popScope();
		currentLoop = null;
	}

	@Override
	public void enterLoopNumArg(LoopNumArgContext ctx) {
		currentLoop.addParameter(new Variable(null, VariableTypes.num));
	}

	@Override
	public void enterLoopVarCallArg(LoopVarCallArgContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();
		Variable parameter = (Variable) symbolTable.resolve(name);
		if (parameter == null) {
			throw new RuntimeException(String.format("Variable '%s' cannot be resolved.", name));
		}
		currentLoop.addParameter(parameter);
	}

	@Override
	public void exitVar_def(Var_defContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();
		VariableTypes type = VariableTypes.var;

		Variable var = new Variable(name, type);
		symbolTable.define(var);
	}

	@Override
	public void enterVar_assign(Var_assignContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();

		if (!symbolTable.isDefined(name)) {
			throw new RuntimeException(String.format("Assignment to undefined variable '%s'.", name));
		}
	}

	@Override
	public void enterVariableCallAtom(VariableCallAtomContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();

		if (!symbolTable.isDefined(name)) {
			throw new RuntimeException(String.format("Variable '%s' cannot be resolved.", name));
		}
	}

	@Override
	public void enterPattern_def(Pattern_defContext ctx) {
		if (ctx.exception != null) {
			return;
		}
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();

		currentPattern = new Pattern(symbolTable.getCurrentScope(), name);
		symbolTable.pushScope(currentPattern, ctx);
	}

	@Override
	public void exitPattern_def(Pattern_defContext ctx) {
		symbolTable.popScope();
		symbolTable.define(currentPattern);
		currentPattern = null;
	}

	@Override
	public void enterNumVarParam(NumVarParamContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();

		Variable parameter = new Variable(name, VariableTypes.num);
		symbolTable.define(parameter);
		currentPattern.addParameter(parameter);
	}

	@Override
	public void enterVarParam(VarParamContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();

		Variable parameter = new Variable(name, VariableTypes.var);
		symbolTable.define(parameter);
		currentPattern.addParameter(parameter);
	}

	@Override
	public void enterGlobalScopeDef(GlobalScopeDefContext ctx) {
		currentPattern.setScope(PatternScopes.global);
	}

	@Override
	public void enterBeforeScopeDef(BeforeScopeDefContext ctx) {
		currentPattern.setScope(PatternScopes.before);
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();
		Variable scopeParameter = new Variable(name, VariableTypes.var);
		symbolTable.define(scopeParameter);
	}

	@Override
	public void enterAfterScopeDef(AfterScopeDefContext ctx) {
		currentPattern.setScope(PatternScopes.after);
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();
		Variable scopeParameter = new Variable(name, VariableTypes.var);
		symbolTable.define(scopeParameter);
	}

	@Override
	public void enterBetweenScopeDef(BetweenScopeDefContext ctx) {
		currentPattern.setScope(PatternScopes.between);
		for (TerminalNode nameNode : ctx.ID()) {
			String name = nameNode.getText();
			Variable scopeParameter = new Variable(name, VariableTypes.var);
			symbolTable.define(scopeParameter);
		}
	}

	@Override
	public void enterAfterUntilScopeDef(AfterUntilScopeDefContext ctx) {
		currentPattern.setScope(PatternScopes.after_until);
		for (TerminalNode nameNode : ctx.ID()) {
			String name = nameNode.getText();
			Variable scopeParameter = new Variable(name, VariableTypes.var);
			symbolTable.define(scopeParameter);
		}
	}

}
