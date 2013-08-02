package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.SeqCallDefinitionContext;
import de.prob.ltl.parser.LtlParser.SeqCallSimpleContext;
import de.prob.ltl.parser.LtlParser.Seq_callContext;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class SeqCall extends AbstractSemanticObject {

	private Seq_callContext context;

	private List<Argument> arguments = new LinkedList<Argument>();
	private Argument withoutArgument;

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
			arguments.add(argument);
		} else {
			SeqCallDefinitionContext ctx = (SeqCallDefinitionContext) context;
			token = ctx.SEQ_VAR().getSymbol();

			int size = ctx.argument().size();
			if (ctx.SEQ_WITHOUT() != null) {
				size -= 1;
				// Check without argument
				withoutArgument = new Argument(parser, ctx.argument(size));

				VariableTypes types[] = new VariableTypes[] { VariableTypes.var, VariableTypes.seq };
				withoutArgument.checkArgument(types, false, true, true);
			}
			for (int i = 0; i < size; i++) {
				Argument argument = new Argument(parser, ctx.argument(i));
				arguments.add(argument);

				VariableTypes types[] = new VariableTypes[] { VariableTypes.var };
				argument.checkArgument(types, false, false, true);
			}
		}
	}

	public Seq_callContext getContext() {
		return context;
	}

	public List<Argument> getArguments() {
		return arguments;
	}

	public Argument getWithoutArgument() {
		return withoutArgument;
	}

}
