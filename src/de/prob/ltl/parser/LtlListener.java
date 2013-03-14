// Generated from Ltl.g4 by ANTLR 4.0

package de.prob.ltl.parser;

import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface LtlListener extends ParseTreeListener {
	void enterParenthesisExpression(LtlParser.ParenthesisExpressionContext ctx);
	void exitParenthesisExpression(LtlParser.ParenthesisExpressionContext ctx);

	void enterConstant(LtlParser.ConstantContext ctx);
	void exitConstant(LtlParser.ConstantContext ctx);

	void enterStart(LtlParser.StartContext ctx);
	void exitStart(LtlParser.StartContext ctx);

	void enterConstantExpression(LtlParser.ConstantExpressionContext ctx);
	void exitConstantExpression(LtlParser.ConstantExpressionContext ctx);

	void enterUnaryExpression(LtlParser.UnaryExpressionContext ctx);
	void exitUnaryExpression(LtlParser.UnaryExpressionContext ctx);
}