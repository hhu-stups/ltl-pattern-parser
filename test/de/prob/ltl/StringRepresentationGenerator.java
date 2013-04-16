package de.prob.ltl;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

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
import de.prob.ltl.parser.LtlParser.PatternCallExpressionContext;
import de.prob.ltl.parser.LtlParser.PatternParamExpressionContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
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
		TerminalNode combined = ctx.UNARY_COMBINED();

		if (combined != null) {
			String value = combined.getText();

			for (int i = 0; i < value.length(); i++) {
				int type = 0;
				switch (value.charAt(i)) {
				case 'G':
					type = LtlLexer.GLOBALLY;
					break;
				case 'F':
					type = LtlLexer.FINALLY;
					break;
				case 'X':
					type = LtlLexer.NEXT;
					break;
				case 'H':
					type = LtlLexer.HISTORICALLY;
					break;
				case 'O':
					type = LtlLexer.ONCE;
					break;
				case 'Y':
					type = LtlLexer.YESTERDAY;
					break;
				}
				builder.append(LtlLexer.ruleNames[type - 1].toLowerCase());
				builder.append('(');
			}
			visit(ctx.expression());
			for (int i = 0; i < value.length(); i++) {
				builder.append(')');
			}
		} else {
			String name = LtlLexer.ruleNames[ctx.unary_op.getType() - 1];
			unaryOp(name.toLowerCase(), ctx.expression());
		}
		return null;
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

	@Override
	public Void visitPatternParamExpression(PatternParamExpressionContext ctx) {
		builder.append("param(\"");
		builder.append(ctx.PATTERN_ID().getText());
		builder.append("\")");
		return null;
	}

	@Override
	public Void visitPatternCallExpression(PatternCallExpressionContext ctx) {
		builder.append("pattern_call(\"");
		builder.append(ctx.PATTERN_ID().getText());
		builder.append("\",");
		visit(ctx.expression());
		builder.append("\")");
		return null;
	}

	@Override
	public Void visitPattern_def(Pattern_defContext ctx) {
		builder.append("pattern_def(\"");
		builder.append(ctx.PATTERN_ID(0).getText());
		builder.append("\",[\"");
		builder.append(ctx.PATTERN_ID(1).getText());
		builder.append("\"");
		if (ctx.PATTERN_ID().size() == 3) {
			builder.append(",\"");
			builder.append(ctx.PATTERN_ID(2).getText());
			builder.append("\"");
		}
		builder.append("],");
		visit(ctx.expression());
		builder.append(")");
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
