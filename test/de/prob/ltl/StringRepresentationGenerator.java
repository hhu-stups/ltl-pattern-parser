package de.prob.ltl;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlBaseVisitor;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.ActionExpressionContext;
import de.prob.ltl.parser.LtlParser.BinaryExpressionContext;
import de.prob.ltl.parser.LtlParser.ConstantExpressionContext;
import de.prob.ltl.parser.LtlParser.EnabledExpressionContext;
import de.prob.ltl.parser.LtlParser.PredicateExpressionContext;
import de.prob.ltl.parser.LtlParser.UnaryExpressionContext;
import de.prob.parserbase.ProBParseException;
import de.prob.parserbase.ProBParserBase;
import de.prob.parserbase.UnparsedParserBase;
import de.prob.prolog.output.PrologTermOutput;
import de.prob.prolog.output.StructuredPrologOutput;
import de.prob.prolog.term.PrologTerm;

public class StringRepresentationGenerator extends LtlBaseVisitor<Void>{

	private final StringBuilder builder = new StringBuilder();
	private final LtlParser parser;

	public StringRepresentationGenerator(LtlParser parser) {
		this.parser = parser;
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
		ParseTree op = ctx.unary_op().getChild(0);
		if (op instanceof TerminalNode) {
			builder.append(LtlParser.tokenNames[((TerminalNode)op).getSymbol().getType()].toLowerCase());
		} else {
			throw new RuntimeException("Child of unary_op has to be a terminal node");
		}
		builder.append('(');
		visit(ctx.getChild(1));
		builder.append(')');
		return null;
	}

	@Override
	public Void visitBinaryExpression(BinaryExpressionContext ctx) {
		ParseTree op = ctx.binary_op().getChild(0);
		if (op instanceof TerminalNode) {
			builder.append(LtlParser.tokenNames[((TerminalNode)op).getSymbol().getType()].toLowerCase());
		} else {
			throw new RuntimeException("Child of binary_op has to be a terminal node");
		}
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
		ProBParserBase parserBase = parser.getParserBase();
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
		ProBParserBase parserBase = parser.getParserBase();
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
		ProBParserBase parserBase = parser.getParserBase();
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

	public String getGeneratedString() {
		return builder.toString();
	}

}
