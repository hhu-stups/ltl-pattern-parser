package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.SeqDefinitionContext;
import de.prob.ltl.parser.LtlParser.SeqVarExtensionContext;
import de.prob.ltl.parser.LtlParser.Seq_defContext;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class SeqDefinition extends AbstractSemanticObject {

	private Seq_defContext context;

	private Variable variable;
	private List<Argument> arguments = new LinkedList<Argument>();
	private List<Argument> withoutArguments = new LinkedList<Argument>();

	public SeqDefinition(LtlParser parser, Seq_defContext context) {
		super(parser);

		this.context = context;
		if (this.context != null) {
			checkArguments();
		}
	}

	private void checkArguments() {
		if(context instanceof SeqVarExtensionContext) {
			TerminalNode node = ((SeqVarExtensionContext) context).ID();
			token = node.getSymbol();

			// check ID
			variable = resolveVariable(node);
			if (variable != null) {
				if (!variable.getType().equals(VariableTypes.seq)) {
					notifyErrorListeners("The type of the variable '%s' is not allowed. Expected type: %s", variable, VariableTypes.seq);
				}
			}

			// Check without argument
			Argument withoutArgument = new Argument(parser, ((SeqVarExtensionContext) context).argument());
			withoutArguments.add(withoutArgument);

			VariableTypes types[] = new VariableTypes[] { VariableTypes.var, VariableTypes.seq };
			withoutArgument.checkArgument(types, false, true, true);
		} else {
			SeqDefinitionContext ctx = (SeqDefinitionContext) context;
			token = ctx.LEFT_PAREN().getSymbol();	// TODO create token from starttoken and stoptoken

			int size = ctx.argument().size();
			if (ctx.SEQ_WITHOUT() != null) {
				size -= 1;
				// Check without argument
				Argument withoutArgument = new Argument(parser, ctx.argument(size));
				withoutArguments.add(withoutArgument);

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

	public void collectArguments() {
		if (variable != null) {
			// Add arguments from variable
			SeqDefinition vDefinition = variable.getSeqValue();
			arguments.addAll(vDefinition.getArguments());
			withoutArguments.addAll(vDefinition.getWithoutArguments());
		}
	}

	public void addArgument(Argument argument) {
		arguments.add(argument);
	}

	public void addWithoutArgument(Argument argument) {
		withoutArguments.add(argument);
	}

	public List<Argument> getArguments() {
		return arguments;
	}

	public List<Argument> getWithoutArguments() {
		return withoutArguments;
	}

}
