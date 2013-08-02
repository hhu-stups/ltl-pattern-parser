package de.prob.ltl.parser.semantic;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.SeqCallDefinitionContext;
import de.prob.ltl.parser.LtlParser.SeqCallSimpleContext;
import de.prob.ltl.parser.LtlParser.Seq_callContext;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class SeqCall extends AbstractSemanticObject {

	private Seq_callContext context;

	private SeqDefinition seq;

	public SeqCall(LtlParser parser, Seq_callContext context) {
		super(parser);

		this.context = context;
		if (this.context != null) {
			checkArguments();
		}
	}

	private void checkArguments() {
		if(context instanceof SeqCallSimpleContext) {
			token = ((SeqCallSimpleContext) context).SEQ_VAR().getSymbol();
			Argument argument = new Argument(parser, ((SeqCallSimpleContext) context).argument());
			argument.checkArgument(new VariableTypes[] { VariableTypes.seq }, false, true, false);
			seq = argument.getSeq();
		} else {
			SeqCallDefinitionContext ctx = (SeqCallDefinitionContext) context;
			token = ctx.SEQ_VAR().getSymbol();
			seq = new SeqDefinition(parser, null);

			int size = ctx.argument().size();
			if (ctx.SEQ_WITHOUT() != null) {
				size -= 1;
				// Check without argument
				Argument withoutArgument = new Argument(parser, ctx.argument(size));
				seq.addWithoutArgument(withoutArgument);

				VariableTypes types[] = new VariableTypes[] { VariableTypes.var, VariableTypes.seq };
				withoutArgument.checkArgument(types, false, true, true);
			}
			for (int i = 0; i < size; i++) {
				Argument argument = new Argument(parser, ctx.argument(i));
				seq.addArgument(argument);

				VariableTypes types[] = new VariableTypes[] { VariableTypes.var };
				argument.checkArgument(types, false, false, true);
			}
		}
	}

	public SeqDefinition getSeq() {
		return seq;
	}

}
