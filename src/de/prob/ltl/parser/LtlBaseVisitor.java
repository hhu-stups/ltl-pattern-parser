// Generated from Ltl.g4 by ANTLR 4.0

package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.ParserRuleContext;

public class LtlBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements LtlVisitor<T> {
	@Override public T visitParenthesisExpression(LtlParser.ParenthesisExpressionContext ctx) { return visitChildren(ctx); }

	@Override public T visitConstant(LtlParser.ConstantContext ctx) { return visitChildren(ctx); }

	@Override public T visitStart(LtlParser.StartContext ctx) { return visitChildren(ctx); }

	@Override public T visitConstantExpression(LtlParser.ConstantExpressionContext ctx) { return visitChildren(ctx); }

	@Override public T visitUnaryExpression(LtlParser.UnaryExpressionContext ctx) { return visitChildren(ctx); }
}