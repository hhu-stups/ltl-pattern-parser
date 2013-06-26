package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser.ActionAtomContext;
import de.prob.ltl.parser.LtlParser.AndExprContext;
import de.prob.ltl.parser.LtlParser.BooleanAtomContext;
import de.prob.ltl.parser.LtlParser.EnabledAtomContext;
import de.prob.ltl.parser.LtlParser.FinallyExprContext;
import de.prob.ltl.parser.LtlParser.GloballyExprContext;
import de.prob.ltl.parser.LtlParser.HistoricallyExprContext;
import de.prob.ltl.parser.LtlParser.ImpliesExprContext;
import de.prob.ltl.parser.LtlParser.NextExprContext;
import de.prob.ltl.parser.LtlParser.NotExprContext;
import de.prob.ltl.parser.LtlParser.OnceExprContext;
import de.prob.ltl.parser.LtlParser.OrExprContext;
import de.prob.ltl.parser.LtlParser.PredicateAtomContext;
import de.prob.ltl.parser.LtlParser.ReleaseExprContext;
import de.prob.ltl.parser.LtlParser.SinceExprContext;
import de.prob.ltl.parser.LtlParser.StateAtomContext;
import de.prob.ltl.parser.LtlParser.TriggerExprContext;
import de.prob.ltl.parser.LtlParser.UnaryCombinedExprContext;
import de.prob.ltl.parser.LtlParser.UntilExprContext;
import de.prob.ltl.parser.LtlParser.WeakuntilExprContext;
import de.prob.ltl.parser.LtlParser.YesterdayExprContext;
import de.prob.parserbase.ProBParseException;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;

public class LtlPrologTermGenerator extends LtlBaseListener {

	private final IPrologTermOutput pto;
	private final String currentStateID;
	private final ProBParserBase specParser;
	private ParseTreeProperty<Integer> numberOfTerms = new ParseTreeProperty<Integer>();

	public LtlPrologTermGenerator(final IPrologTermOutput pto, String currentStateID, final ProBParserBase specParser) {
		this.pto = pto;
		this.currentStateID = currentStateID;
		this.specParser = specParser;
	}

	public void generatePrologTerm(ParseTree ast) {
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, ast);
	}

	@Override
	public void enterNotExpr(NotExprContext ctx) {
		pto.openTerm("not");
	}

	@Override
	public void exitNotExpr(NotExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterFinallyExpr(FinallyExprContext ctx) {
		pto.openTerm("finally");
	}

	@Override
	public void exitFinallyExpr(FinallyExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterHistoricallyExpr(HistoricallyExprContext ctx) {
		pto.openTerm("historically");
	}

	@Override
	public void exitHistoricallyExpr(HistoricallyExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterOnceExpr(OnceExprContext ctx) {
		pto.openTerm("once");
	}

	@Override
	public void exitOnceExpr(OnceExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterNextExpr(NextExprContext ctx) {
		pto.openTerm("next");
	}

	@Override
	public void exitNextExpr(NextExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterYesterdayExpr(YesterdayExprContext ctx) {
		pto.openTerm("yesterday");
	}

	@Override
	public void exitYesterdayExpr(YesterdayExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterGloballyExpr(GloballyExprContext ctx) {
		pto.openTerm("globally");
	}

	@Override
	public void exitGloballyExpr(GloballyExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterUnaryCombinedExpr(UnaryCombinedExprContext ctx) {
		TerminalNode node = ctx.UNARY_COMBINED();
		String ops = node.getText();
		numberOfTerms.put(ctx, ops.length());

		for (char c : ops.toCharArray()) {
			switch (c) {
			case 'G':
				enterGloballyExpr(null);
				break;
			case 'F':
				enterFinallyExpr(null);
				break;
			case 'X':
				enterNextExpr(null);
				break;
			case 'H':
				enterHistoricallyExpr(null);
				break;
			case 'O':
				enterOnceExpr(null);
				break;
			case 'Y':
				enterYesterdayExpr(null);
				break;
			}
		}
	}

	@Override
	public void exitUnaryCombinedExpr(UnaryCombinedExprContext ctx) {
		int num = numberOfTerms.get(ctx);
		for (int i = 0; i < num; i++) {
			pto.closeTerm();
		}
		numberOfTerms.removeFrom(ctx);
	}

	@Override
	public void enterUntilExpr(UntilExprContext ctx) {
		pto.openTerm("until");
	}

	@Override
	public void exitUntilExpr(UntilExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterTriggerExpr(TriggerExprContext ctx) {
		pto.openTerm("trigger");
	}

	@Override
	public void exitTriggerExpr(TriggerExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterSinceExpr(SinceExprContext ctx) {
		pto.openTerm("since");
	}

	@Override
	public void exitSinceExpr(SinceExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterWeakuntilExpr(WeakuntilExprContext ctx) {
		pto.openTerm("weakuntil");
	}

	@Override
	public void exitWeakuntilExpr(WeakuntilExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterReleaseExpr(ReleaseExprContext ctx) {
		pto.openTerm("release");
	}

	@Override
	public void exitReleaseExpr(ReleaseExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterAndExpr(AndExprContext ctx) {
		pto.openTerm("and");
	}

	@Override
	public void exitAndExpr(AndExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterOrExpr(OrExprContext ctx) {
		pto.openTerm("or");
	}

	@Override
	public void exitOrExpr(OrExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterImpliesExpr(ImpliesExprContext ctx) {
		pto.openTerm("implies");
	}

	@Override
	public void exitImpliesExpr(ImpliesExprContext ctx) {
		pto.closeTerm();
	}

	@Override
	public void enterPredicateAtom(PredicateAtomContext ctx) {
		pto.openTerm("ap");
		try {
			String text = ctx.getText();
			specParser.parsePredicate(pto, text.substring(1, text.length() - 1), true);
		} catch (ProBParseException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedOperationException e) {
			throw e;
		}
		pto.closeTerm();
	}

	@Override
	public void enterActionAtom(ActionAtomContext ctx) {
		pto.openTerm("action");
		String text = ctx.getText();
		parseTransitionPredicate(text.substring(1, text.length() - 1));
		pto.closeTerm();
	}

	@Override
	public void enterEnabledAtom(EnabledAtomContext ctx) {
		pto.openTerm("ap");
		pto.openTerm("enabled");
		String text = ctx.getText();
		parseTransitionPredicate(text.substring(2, text.length() - 1));
		pto.closeTerm();
		pto.closeTerm();
	}

	@Override
	public void enterStateAtom(StateAtomContext ctx) {
		pto.openTerm("ap");
		if (ctx.CURRENT() != null) {
			if (currentStateID != null) {
				pto.openTerm("stateid");
				pto.printAtomOrNumber(currentStateID);
				pto.closeTerm();
			} else {
				pto.printAtom("current");
			}
		} else if (ctx.DEADLOCK() != null) {
			pto.printAtom("deadlock");
		} else {
			pto.printAtom("sink");
		}
		pto.closeTerm();
	}

	@Override
	public void enterBooleanAtom(BooleanAtomContext ctx) {
		if (ctx.TRUE() != null) {
			pto.printAtom("true");
		} else {
			pto.printAtom("false");
		}
	}

	protected void parseTransitionPredicate(final String text) {
		try {
			specParser.parseTransitionPredicate(pto, text, true);
		} catch (ProBParseException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedOperationException e) {
			throw e;
		}
	}

}
