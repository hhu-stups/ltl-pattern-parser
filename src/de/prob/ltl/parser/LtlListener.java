// Generated from Ltl.g4 by ANTLR 4.0

package de.prob.ltl.parser;
import de.prob.parserbase.ProBParserBase;

import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface LtlListener extends ParseTreeListener {
	void enterParenthesisExpression(LtlParser.ParenthesisExpressionContext ctx);
	void exitParenthesisExpression(LtlParser.ParenthesisExpressionContext ctx);

	void enterConstant(LtlParser.ConstantContext ctx);
	void exitConstant(LtlParser.ConstantContext ctx);

	void enterStart(LtlParser.StartContext ctx);
	void exitStart(LtlParser.StartContext ctx);

	void enterBinary_op(LtlParser.Binary_opContext ctx);
	void exitBinary_op(LtlParser.Binary_opContext ctx);

	void enterConstantExpression(LtlParser.ConstantExpressionContext ctx);
	void exitConstantExpression(LtlParser.ConstantExpressionContext ctx);

	void enterUnaryExpression(LtlParser.UnaryExpressionContext ctx);
	void exitUnaryExpression(LtlParser.UnaryExpressionContext ctx);

	void enterActionExpression(LtlParser.ActionExpressionContext ctx);
	void exitActionExpression(LtlParser.ActionExpressionContext ctx);

	void enterUnary_op(LtlParser.Unary_opContext ctx);
	void exitUnary_op(LtlParser.Unary_opContext ctx);

	void enterEnabledExpression(LtlParser.EnabledExpressionContext ctx);
	void exitEnabledExpression(LtlParser.EnabledExpressionContext ctx);

	void enterPredicateExpression(LtlParser.PredicateExpressionContext ctx);
	void exitPredicateExpression(LtlParser.PredicateExpressionContext ctx);

	void enterBinaryExpression(LtlParser.BinaryExpressionContext ctx);
	void exitBinaryExpression(LtlParser.BinaryExpressionContext ctx);
}