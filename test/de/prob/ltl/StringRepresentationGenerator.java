package de.prob.ltl;

import de.prob.ltl.parser.LtlBaseVisitor;
import de.prob.ltl.parser.LtlParser;
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
		builder.append(LtlParser.tokenNames[ctx.unary_op.getType()].toLowerCase());
		builder.append('(');
		visit(ctx.getChild(1));
		builder.append(')');
		return null;
	}

	public String getGeneratedString() {
		return builder.toString();
	}

}
