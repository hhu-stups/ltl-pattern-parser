package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.prob.ltl.parser.LtlBaseListener;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Pattern_callContext;
import de.prob.ltl.parser.LtlParser.VariableCallAtomContext;

public class ExprOrAtomCheck extends LtlBaseListener {

	private final LtlParser parser;

	private ParseTree context;

	public ExprOrAtomCheck(LtlParser parser, ParseTree context) {
		this.parser = parser;
		this.context = context;

		if (this.context != null) {
			ParseTreeWalker.DEFAULT.walk(this, this.context);
		}
	}

	@Override
	public void enterVariableCallAtom(VariableCallAtomContext ctx) {
		new VariableCall(parser, ctx);
	}

	@Override
	public void enterPattern_call(Pattern_callContext ctx) {
		new PatternCall(parser, ctx);
	}

}
