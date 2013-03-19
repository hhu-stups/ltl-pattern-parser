package de.prob.ltl;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlBaseVisitor;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.ActionExpressionContext;
import de.prob.ltl.parser.LtlParser.AndExpressionContext;
import de.prob.ltl.parser.LtlParser.BinaryExpressionContext;
import de.prob.ltl.parser.LtlParser.ConstantExpressionContext;
import de.prob.ltl.parser.LtlParser.EnabledExpressionContext;
import de.prob.ltl.parser.LtlParser.ImpliesExpressionContext;
import de.prob.ltl.parser.LtlParser.NegateExpressionContext;
import de.prob.ltl.parser.LtlParser.OrExpressionContext;
import de.prob.ltl.parser.LtlParser.PredicateExpressionContext;
import de.prob.ltl.parser.LtlParser.UnaryExpressionContext;
import de.prob.parserbase.ProBParseException;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.StructuredPrologOutput;
import de.prob.prolog.term.PrologTerm;

public class StringRepresentationGenerator extends LtlBaseVisitor<Void>{

	private final StringBuilder builder = new StringBuilder();
	private final ProBParserBase parserBase;

	public StringRepresentationGenerator(ProBParserBase parserBase) {
		this.parserBase = parserBase;
	}

	@Override
	public Void visitConstantExpression(ConstantExpressionContext ctx) {
		String name = ctx.constant().getText();
		if (ctx.constant().TRUE() == null && ctx.constant().FALSE() == null) {
			if (ctx.constant().CURRENT() != null) {
				name = "stateid(current)";
			}
			name = String.format("ap(%s)", name);
		}
		builder.append(name);
		return null;
	}

	@Override
	public Void visitUnaryExpression(UnaryExpressionContext ctx) {
		builder.append(LtlParser.tokenNames[ctx.unary_op.getType()].toLowerCase());
		builder.append('(');
		visit(ctx.getChild(1));
		builder.append(')');
		return null;
	}

	@Override
	public Void visitBinaryExpression(BinaryExpressionContext ctx) {
		builder.append(LtlParser.tokenNames[ctx.binary_op.getType()].toLowerCase());
		builder.append('(');
		visit(ctx.getChild(0));
		builder.append(',');
		visit(ctx.getChild(2));
		builder.append(')');
		return null;
	}

	@Override
	public Void visitPredicateExpression(PredicateExpressionContext ctx) {
		builder.append("ap(");
		String predicate = ctx.getText();
		if (parserBase != null) {
			StructuredPrologOutput pto = new StructuredPrologOutput();
			try {
				parserBase.parsePredicate(pto, predicate.substring(1, predicate.length() - 1), true);
				pto.fullstop();
				PrologTerm term = pto.getSentences().iterator().next();
				builder.append(term.toString());
			} catch (UnsupportedOperationException | ProBParseException e) {
				e.printStackTrace();
			}
		} else {
			builder.append(predicate);
		}
		builder.append(")");
		return null;
	}

	@Override
	public Void visitActionExpression(ActionExpressionContext ctx) {
		builder.append("action(");
		String predicate = ctx.getText();
		if (parserBase != null) {
			StructuredPrologOutput pto = new StructuredPrologOutput();
			try {
				parserBase.parseTransitionPredicate(pto, predicate.substring(1, predicate.length() - 1), true);
				pto.fullstop();
				PrologTerm term = pto.getSentences().iterator().next();
				builder.append(term.toString());
			} catch (UnsupportedOperationException | ProBParseException e) {
				e.printStackTrace();
			}
		} else {
			builder.append(predicate);
		}
		builder.append(")");
		return null;
	}

	@Override
	public Void visitEnabledExpression(EnabledExpressionContext ctx) {
		builder.append("ap(enabled(");
		String predicate = ctx.getText();
		if (parserBase != null) {
			StructuredPrologOutput pto = new StructuredPrologOutput();
			try {
				parserBase.parseTransitionPredicate(pto, predicate.substring(2, predicate.length() - 1), true);
				pto.fullstop();
				PrologTerm term = pto.getSentences().iterator().next();
				builder.append(term.toString());
			} catch (UnsupportedOperationException | ProBParseException e) {
				e.printStackTrace();
			}
		} else {
			builder.append(predicate);
		}
		builder.append("))");
		return null;
	}



	@Override
	public Void visitNegateExpression(NegateExpressionContext ctx) {
		builder.append("not(");
		visit(ctx.getChild(1));
		builder.append(')');
		return null;
	}

	@Override
	public Void visitAndExpression(AndExpressionContext ctx) {
		builder.append("and");
		builder.append('(');
		visit(ctx.getChild(0));
		builder.append(',');
		visit(ctx.getChild(2));
		builder.append(')');
		return null;
	}

	@Override
	public Void visitOrExpression(OrExpressionContext ctx) {
		builder.append("or(");
		visit(ctx.getChild(0));
		builder.append(',');
		visit(ctx.getChild(2));
		builder.append(')');
		return null;
	}

	@Override
	public Void visitImpliesExpression(ImpliesExpressionContext ctx) {
		builder.append("implies(");
		visit(ctx.getChild(0));
		builder.append(',');
		visit(ctx.getChild(2));
		builder.append(')');
		return null;
	}

	public String getGeneratedString() {
		return builder.toString();
	}

}
