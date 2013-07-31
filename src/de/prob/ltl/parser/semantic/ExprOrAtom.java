package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.prob.ltl.parser.LtlBaseListener;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Pattern_callContext;
import de.prob.ltl.parser.LtlParser.Scope_callContext;
import de.prob.ltl.parser.LtlParser.SeqCallAtomContext;
import de.prob.ltl.parser.LtlParser.VariableCallAtomContext;

public class ExprOrAtom implements Node {

	private ParseTree context;

	public ExprOrAtom(final LtlParser parser, ParseTree context) {
		this.context = context;

		if (this.context != null) {
			ParseTreeWalker.DEFAULT.walk(new LtlBaseListener() {
				@Override
				public void enterVariableCallAtom(VariableCallAtomContext ctx) {
					new VariableCall(parser, ctx);
				}

				@Override
				public void enterPattern_call(Pattern_callContext ctx) {
					new PatternCall(parser, ctx);
				}

				@Override
				public void enterScope_call(Scope_callContext ctx) {
					new ScopeCall(parser, ctx);
				}

				@Override
				public void enterSeqCallAtom(SeqCallAtomContext ctx) {
					new SeqCall(parser, ctx.seq_call());
				}

			}, this.context);
		}
	}

	public ParseTree getContext() {
		return context;
	}

}
