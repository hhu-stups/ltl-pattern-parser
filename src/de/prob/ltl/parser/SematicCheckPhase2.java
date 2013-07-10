package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser.AfterScopeCallContext;
import de.prob.ltl.parser.LtlParser.AfterUntilScopeCallContext;
import de.prob.ltl.parser.LtlParser.BeforeScopeCallContext;
import de.prob.ltl.parser.LtlParser.BetweenScopeCallContext;
import de.prob.ltl.parser.LtlParser.ExprArgContext;
import de.prob.ltl.parser.LtlParser.GlobalScopeCallContext;
import de.prob.ltl.parser.LtlParser.LoopContext;
import de.prob.ltl.parser.LtlParser.LoopNumArgContext;
import de.prob.ltl.parser.LtlParser.LoopVarCallArgContext;
import de.prob.ltl.parser.LtlParser.NumArgContext;
import de.prob.ltl.parser.LtlParser.Pattern_callContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.LtlParser.VarCallArgContext;
import de.prob.ltl.parser.symboltable.Pattern;
import de.prob.ltl.parser.symboltable.Pattern.PatternScopes;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.ltl.parser.symboltable.Variable.VariableTypes;

public class SematicCheckPhase2 extends LtlBaseListener {

	private LtlParser parser;
	private SymbolTable symbolTable;
	private Pattern currentPattern;

	public SematicCheckPhase2(LtlParser parser) {
		this.parser = parser;
		this.symbolTable = parser.getSymbolTable();
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
	public void enterLoop(LoopContext ctx) {
		symbolTable.pushScope(ctx);
	}

	@Override
	public void exitLoop(LoopContext ctx) {
		symbolTable.popScope();
	}

	@Override
	public void enterLoopNumArg(LoopNumArgContext ctx) {
	}

	@Override
	public void enterLoopVarCallArg(LoopVarCallArgContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();
		Variable arg = (Variable) symbolTable.resolve(name);
		if (arg.getType().equals(VariableTypes.var)) {
			parser.notifyErrorListeners(nameNode.getSymbol(), String.format("The variable '%s' has the wrong type. Only number variables are allowed.", name), null);
		}
	}

	@Override
	public void exitPattern_call(Pattern_callContext ctx) {
		if (!symbolTable.isDefined(currentPattern)) {
			parser.notifyErrorListeners(ctx.ID().getSymbol(), String.format("Pattern '%s' cannot be resolved.", currentPattern.getSymbolID()), null);
		}
		try {
			symbolTable.checkTypes(currentPattern);
		} catch(RuntimeException e) {
			parser.notifyErrorListeners(ctx.ID().getSymbol(), e.getMessage(), null);
		}
	}

	@Override
	public void enterPattern_call(Pattern_callContext ctx) {
		if (ctx.exception != null) {
			return;
		}
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();

		currentPattern = new Pattern(symbolTable.getCurrentScope(), name);
	}

	@Override
	public void enterNumArg(NumArgContext ctx) {
		currentPattern.addParameter(new Variable(null, VariableTypes.num));
	}

	@Override
	public void enterVarCallArg(VarCallArgContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();
		Variable arg = (Variable) symbolTable.resolve(name);
		currentPattern.addParameter(new Variable(null, arg.getType()));
	}

	@Override
	public void enterExprArg(ExprArgContext ctx) {
		currentPattern.addParameter(new Variable(null, VariableTypes.var));
	}

	@Override
	public void enterGlobalScopeCall(GlobalScopeCallContext ctx) {
		currentPattern.setScope(PatternScopes.global);
	}

	@Override
	public void enterBeforeScopeCall(BeforeScopeCallContext ctx) {
		currentPattern.setScope(PatternScopes.before);
	}

	@Override
	public void enterAfterScopeCall(AfterScopeCallContext ctx) {
		currentPattern.setScope(PatternScopes.after);
	}

	@Override
	public void enterBetweenScopeCall(BetweenScopeCallContext ctx) {
		currentPattern.setScope(PatternScopes.between);
	}

	@Override
	public void enterAfterUntilScopeCall(AfterUntilScopeCallContext ctx) {
		currentPattern.setScope(PatternScopes.after_until);
	}

}
