package de.prob.ltl;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlBaseVisitor;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.BinaryExpressionContext;
import de.prob.ltl.parser.LtlParser.ConstantExpressionContext;
import de.prob.ltl.parser.LtlParser.UnaryExpressionContext;

public class StringRepresentationGenerator extends LtlBaseVisitor<Void>{

	private final StringBuilder builder = new StringBuilder();

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

	public String getGeneratedString() {
		return builder.toString();
	}

}
