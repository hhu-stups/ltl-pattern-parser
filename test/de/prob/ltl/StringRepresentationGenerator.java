package de.prob.ltl;

import org.antlr.v4.runtime.tree.ParseTree;

import de.prob.ltl.parser.LtlBaseVisitor;
import de.prob.ltl.parser.LtlLexer;
import de.prob.ltl.parser.LtlParser.ActionExpressionContext;
import de.prob.ltl.parser.LtlParser.AndExpressionContext;
import de.prob.ltl.parser.LtlParser.BinaryExpressionContext;
import de.prob.ltl.parser.LtlParser.ConstantContext;
import de.prob.ltl.parser.LtlParser.EnabledExpressionContext;
import de.prob.ltl.parser.LtlParser.ImpliesExpressionContext;
import de.prob.ltl.parser.LtlParser.NotExpressionContext;
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
	public Void visitNotExpression(NotExpressionContext ctx) {
		ParseTree child = ctx.expression();
		return unaryOp("not", child);
	}

	@Override
	public Void visitAndExpression(AndExpressionContext ctx) {
		return binaryOp("and", ctx.expression(0), ctx.expression(1));
	}

	@Override
	public Void visitOrExpression(OrExpressionContext ctx) {
		return binaryOp("or", ctx.expression(0), ctx.expression(1));
	}

	@Override
	public Void visitImpliesExpression(ImpliesExpressionContext ctx) {
		return binaryOp("implies", ctx.expression(0), ctx.expression(1));
	}

	@Override
	public Void visitBinaryExpression(BinaryExpressionContext ctx) {
		String name = LtlLexer.ruleNames[ctx.binary_op.getType() - 1];
		return binaryOp(name.toLowerCase(), ctx.expression(0), ctx.expression(1));
	}

	@Override
	public Void visitUnaryExpression(UnaryExpressionContext ctx) {
		String name = LtlLexer.ruleNames[ctx.unary_op.getType() - 1];
		return unaryOp(name.toLowerCase(), ctx.expression());
	}

	@Override
	public Void visitPredicateExpression(PredicateExpressionContext ctx) {
		String predicate = ctx.getText();
		if (parserBase != null) {
			StructuredPrologOutput pto = new StructuredPrologOutput();
			try {
				parserBase.parsePredicate(pto, predicate.substring(1, predicate.length() - 1), true);
				pto.fullstop();
				PrologTerm term = pto.getSentences().iterator().next();
				builder.append("ap(");
				builder.append(term.toString());
				builder.append(")");
			} catch (UnsupportedOperationException | ProBParseException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	@Override
	public Void visitActionExpression(ActionExpressionContext ctx) {
		String predicate = ctx.getText();
		if (parserBase != null) {
			StructuredPrologOutput pto = new StructuredPrologOutput();
			try {
				parserBase.parseTransitionPredicate(pto, predicate.substring(1, predicate.length() - 1), true);
				pto.fullstop();
				PrologTerm term = pto.getSentences().iterator().next();
				builder.append("action(");
				builder.append(term.toString());
				builder.append(")");
			} catch (UnsupportedOperationException | ProBParseException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	@Override
	public Void visitEnabledExpression(EnabledExpressionContext ctx) {
		String predicate = ctx.getText();
		if (parserBase != null) {
			StructuredPrologOutput pto = new StructuredPrologOutput();
			try {
				parserBase.parseTransitionPredicate(pto, predicate.substring(2, predicate.length() - 1), true);
				pto.fullstop();
				PrologTerm term = pto.getSentences().iterator().next();
				builder.append("ap(enabled(");
				builder.append(term.toString());
				builder.append("))");
			} catch (UnsupportedOperationException | ProBParseException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	@Override
	public Void visitConstant(ConstantContext ctx) {
		String name = ctx.getText();
		if (ctx.TRUE() == null && ctx.FALSE() == null) {
			if (ctx.CURRENT() != null) {
				name = "stateid(current)";
			}
			name = String.format("ap(%s)", name);
		}
		builder.append(name);
		return null;
	}

	private Void unaryOp(String name, ParseTree child) {
		builder.append(name);
		builder.append('(');
		visit(child);
		builder.append(')');
		return null;
	}

	private Void binaryOp(String name, ParseTree left, ParseTree right) {
		builder.append(name);
		builder.append('(');
		visit(left);
		builder.append(',');
		visit(right);
		builder.append(')');
		return null;
	}

	public String getGeneratedString() {
		return builder.toString();
	}

}
