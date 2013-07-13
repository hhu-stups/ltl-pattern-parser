package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.prob.ltl.parser.LtlBaseListener;
import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Pattern_callContext;
import de.prob.ltl.parser.LtlParser.VariableCallAtomContext;
import de.prob.ltl.parser.prolog.ExprOrAtomPrologTermGenerator;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;

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
			}, this.context);
		}
	}

	@Override
	public void createPrologTerm(LtlParser parser, IPrologTermOutput pto,
			String currentState, ProBParserBase parserBase) {
		ParseTreeWalker.DEFAULT.walk(new ExprOrAtomPrologTermGenerator(parser, pto, currentState, parserBase), context);
	}

}
