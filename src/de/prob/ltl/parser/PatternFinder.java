package de.prob.ltl.parser;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser.AfterScopeCallContext;
import de.prob.ltl.parser.LtlParser.AfterUntilScopeCallContext;
import de.prob.ltl.parser.LtlParser.BeforeScopeCallContext;
import de.prob.ltl.parser.LtlParser.BetweenScopeCallContext;
import de.prob.ltl.parser.LtlParser.ExprArgContext;
import de.prob.ltl.parser.LtlParser.GlobalScopeCallContext;
import de.prob.ltl.parser.LtlParser.NumArgContext;
import de.prob.ltl.parser.LtlParser.Pattern_callContext;
import de.prob.ltl.parser.LtlParser.VarCallArgContext;
import de.prob.ltl.parser.symboltable.Pattern;
import de.prob.ltl.parser.symboltable.Pattern.PatternScopes;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.ltl.parser.symboltable.Variable.VariableTypes;

public class PatternFinder extends LtlBaseListener {

	private Pattern patternCall;
	private List<ParserRuleContext> arguments = new LinkedList<ParserRuleContext>();

	public Pattern getCalledPattern() {
		return patternCall;
	}

	public List<ParserRuleContext> getArguments() {
		return arguments;
	}

	@Override
	public void enterPattern_call(Pattern_callContext ctx) {
		TerminalNode nameNode = ctx.ID();
		String name = nameNode.getText();

		patternCall = new Pattern(null, name);
	}

	@Override
	public void enterNumArg(NumArgContext ctx) {
		patternCall.addParameter(new Variable(null, VariableTypes.num));
		arguments.add(ctx);
	}

	@Override
	public void enterVarCallArg(VarCallArgContext ctx) {
		patternCall.addParameter(new Variable(null, VariableTypes.var));
		arguments.add(ctx);
	}

	@Override
	public void enterExprArg(ExprArgContext ctx) {
		patternCall.addParameter(new Variable(null, null));
		arguments.add(ctx);
	}

	@Override
	public void enterGlobalScopeCall(GlobalScopeCallContext ctx) {
		patternCall.setScope(PatternScopes.global);
	}

	@Override
	public void enterBeforeScopeCall(BeforeScopeCallContext ctx) {
		patternCall.setScope(PatternScopes.before);
	}

	@Override
	public void enterAfterScopeCall(AfterScopeCallContext ctx) {
		patternCall.setScope(PatternScopes.after);
	}

	@Override
	public void enterBetweenScopeCall(BetweenScopeCallContext ctx) {
		patternCall.setScope(PatternScopes.between);
	}

	@Override
	public void enterAfterUntilScopeCall(AfterUntilScopeCallContext ctx) {
		patternCall.setScope(PatternScopes.after_until);
	}

}
