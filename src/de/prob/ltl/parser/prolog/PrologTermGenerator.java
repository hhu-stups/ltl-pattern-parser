package de.prob.ltl.parser.prolog;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
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
import de.prob.ltl.parser.LtlParser.Loop_bodyContext;
import de.prob.ltl.parser.LtlParser.NumArgContext;
import de.prob.ltl.parser.LtlParser.Pattern_callContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.LtlParser.VarCallArgContext;
import de.prob.ltl.parser.LtlParser.Var_assignContext;
import de.prob.ltl.parser.LtlParser.Var_defContext;
import de.prob.ltl.parser.LtlParser.VariableCallAtomContext;
import de.prob.ltl.parser.symboltable.Loop;
import de.prob.ltl.parser.symboltable.Loop.LoopTypes;
import de.prob.ltl.parser.symboltable.Pattern;
import de.prob.ltl.parser.symboltable.Pattern.PatternScopes;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.output.StructuredPrologOutput;
import de.prob.prolog.term.IntegerPrologTerm;
import de.prob.prolog.term.PrologTerm;

public class PrologTermGenerator extends BasePrologTermGenerator {

	protected SymbolTable symbolTable;
	protected Pattern patternCall;
	protected List<PrologTerm> patternArguments = new LinkedList<PrologTerm>();
	protected Loop loopCall;
	protected List<PrologTerm> loopArguments = new LinkedList<PrologTerm>();
	protected boolean allowPatternDef;

	public PrologTermGenerator(SymbolTable symbolTable, IPrologTermOutput pto,
			String currentStateID, ProBParserBase specParser) {
		super(pto, currentStateID, specParser);
		this.symbolTable = symbolTable;
	}

	private void addPatternArg(PrologTerm arg) {
		patternCall.addParameter(new Variable(null, null));
		patternArguments.add(arg);
	}

	private void setArgumentValues(Pattern definedPattern) {
		if (patternArguments != null && patternArguments.size() > 0) {
			for (int i = 0; i < patternArguments.size(); i++) {
				definedPattern.getParameters().get(i).setValue(patternArguments.get(i));
			}
		} else if (definedPattern.getParameters().size() > 0) {
			throw new RuntimeException("Error when calling pattern. Too few arguments.");
		}
	}

	@Override
	public void enterPattern_call(Pattern_callContext ctx) {
		if (patternCall == null && blockingContext == null) {
			TerminalNode nameNode = ctx.ID();
			String name = nameNode.getText();

			patternCall = new Pattern(null, name);
			patternCall.setDefinitionContext(ctx);
		}
	}

	@Override
	public void exitPattern_call(Pattern_callContext ctx) {
		if (patternCall != null && ctx.equals(patternCall.getDefinitionContext())) {
			Pattern definedPattern = (Pattern) symbolTable.resolve(patternCall.getSymbolID());
			setArgumentValues(definedPattern);

			symbolTable.pushScope(definedPattern);
			StructuredPrologOutput exprPto = new StructuredPrologOutput();
			Pattern_defContext dCtx = (Pattern_defContext) definedPattern.getDefinitionContext();
			ParseTreeWalker.DEFAULT.walk(new PrologTermGenerator(symbolTable, exprPto, currentStateID, specParser), dCtx.pattern_def_body());
			exprPto.fullstop();
			symbolTable.popScope();

			PrologTerm term = exprPto.getSentences().get(0);
			pto.printTerm(term);
			//pto.printTerm(new PatternCallPrologTerm(definedPattern, arguments));

			patternCall = null;
			patternArguments.clear();
		}
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

	@Override
	public void enterVarCallArg(VarCallArgContext ctx) {
		if (enterContext(ctx)) {
			TerminalNode nameNode = ctx.ID();
			String name = nameNode.getText();

			Variable var = (Variable) symbolTable.resolve(name);
			addPatternArg(var.getValue());
		}
	}

	@Override
	public void enterNumArg(NumArgContext ctx) {
		TerminalNode numNode = ctx.NUM();
		String value = numNode.getText();
		addPatternArg(new IntegerPrologTerm(Integer.parseInt(value)));
	}

	@Override
	public void enterExprArg(ExprArgContext ctx) {
		if (enterContext(ctx)) {
			StructuredPrologOutput exprPto = new StructuredPrologOutput();
			ParseTreeWalker.DEFAULT.walk(new PrologTermGenerator(symbolTable, exprPto, currentStateID, specParser), ctx.expr());
			exprPto.fullstop();

			PrologTerm arg = exprPto.getSentences().get(0);
			addPatternArg(arg);
		}
	}

	//#####################################################
	@Override
	public void enterVar_def(Var_defContext ctx) {
		if (enterContext(ctx)) {
			TerminalNode nameNode = ctx.ID();
			String name = nameNode.getText();

			Variable var = (Variable) symbolTable.resolve(name);
			StructuredPrologOutput exprPto = new StructuredPrologOutput();
			ParseTreeWalker.DEFAULT.walk(new PrologTermGenerator(symbolTable, exprPto, currentStateID, specParser), ctx.expr());
			exprPto.fullstop();

			PrologTerm value = exprPto.getSentences().get(0);
			var.setValue(value);
		}
	}

	@Override
	public void enterVar_assign(Var_assignContext ctx) {
		if (enterContext(ctx)) {
			TerminalNode nameNode = ctx.ID();
			String name = nameNode.getText();

			Variable var = (Variable) symbolTable.resolve(name);
			StructuredPrologOutput exprPto = new StructuredPrologOutput();
			ParseTreeWalker.DEFAULT.walk(new PrologTermGenerator(symbolTable, exprPto, currentStateID, specParser), ctx.expr());
			exprPto.fullstop();

			PrologTerm value = exprPto.getSentences().get(0);
			var.setValue(value);
		}
	}

	@Override
	public void enterVariableCallAtom(VariableCallAtomContext ctx) {
		if (enterContext(ctx)) {
			TerminalNode nameNode = ctx.ID();
			String name = nameNode.getText();

			Variable var = (Variable) symbolTable.resolve(name);
			pto.printTerm(var.getValue());
		}
	}

	// ################################################
	@Override
	public void enterLoop(LoopContext ctx) {
		if (blockingContext == null) {
			symbolTable.pushScope(ctx);
			loopCall = (Loop) symbolTable.getCurrentScope();
		}
	}

	@Override
	public void enterLoop_body(Loop_bodyContext ctx) {
		if (loopCall != null) {
			enterContext(ctx);
		}
	}

	private void loopStep(ParserRuleContext ctx) {
		StructuredPrologOutput exprPto = new StructuredPrologOutput();
		ParseTreeWalker.DEFAULT.walk(new PrologTermGenerator(symbolTable, exprPto, currentStateID, specParser), ctx);
	}

	@Override
	public void exitLoop(LoopContext ctx) {
		if (loopCall != null && ctx.equals(loopCall.getDefinitionContext())) {
			BigInteger begin = null;
			BigInteger end = null;

			PrologTerm beginTerm = loopArguments.get(0);
			PrologTerm endTerm = loopArguments.get(1);
			if (beginTerm.isNumber()) {
				begin = ((IntegerPrologTerm) beginTerm).getValue();
			}
			if (endTerm.isNumber()) {
				end = ((IntegerPrologTerm) endTerm).getValue();
			}

			if (loopCall.getType().equals(LoopTypes.up)){
				while (begin.compareTo(end) == -1) {
					loopStep(ctx.loop_body());
					begin = begin.add(BigInteger.ONE);
				}
			} else {
				while (begin.compareTo(end) == 1) {
					loopStep(ctx.loop_body());
					begin = begin.subtract(BigInteger.ONE);
				}
			}

			symbolTable.popScope();
			loopCall = null;
			loopArguments.clear();
		}
	}

	@Override
	public void enterLoopNumArg(LoopNumArgContext ctx) {
		if (enterContext(ctx)) {
			TerminalNode numNode = ctx.NUM();
			String value = numNode.getText();
			loopArguments.add(new IntegerPrologTerm(Integer.parseInt(value)));
		}
	}

	@Override
	public void enterLoopVarCallArg(LoopVarCallArgContext ctx) {
		if (enterContext(ctx)) {
			TerminalNode nameNode = ctx.ID();
			String name = nameNode.getText();

			Variable var = (Variable) symbolTable.resolve(name);
			loopArguments.add(var.getValue());
		}
	}

}
