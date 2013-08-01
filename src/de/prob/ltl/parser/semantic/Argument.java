package de.prob.ltl.parser.semantic;

import java.util.Arrays;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.ArgumentContext;
import de.prob.ltl.parser.LtlParser.ExprArgumentContext;
import de.prob.ltl.parser.LtlParser.NumArgumentContext;
import de.prob.ltl.parser.LtlParser.ParArgumentContext;
import de.prob.ltl.parser.LtlParser.SeqArgumentContext;
import de.prob.ltl.parser.LtlParser.VarArgumentContext;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class Argument extends AbstractSemanticObject {

	private ArgumentContext context;

	public Argument(LtlParser parser, ArgumentContext context) {
		super(parser);

		this.context = context;
	}

	public void checkArgument(VariableTypes[] allowedVariableTypes, boolean numAllowed, boolean seqDefinitionAllowed, boolean exprAllowed) {
		if (context instanceof VarArgumentContext) {
			token = ((VarArgumentContext) context).ID().getSymbol();
			if (allowedVariableTypes == null) {
				// Variable arguments are not allowed
				notifyErrorListeners("A variable argument is not allowed.");
			} else {
				Variable variable = resolveVariable(((VarArgumentContext) context).ID());
				if (variable != null) {
					boolean typeFound = false;
					// Check if type is allowed
					for (VariableTypes type : allowedVariableTypes) {
						if (type.equals(variable.getType())) {
							typeFound = true;
							break;
						}
					}
					if (!typeFound) {
						notifyErrorListeners("The type of the variable argument '%s' is not allowed. Expected type(s): %s", variable, Arrays.toString(allowedVariableTypes));
					} else {
						// TODO get value
					}
				}
			}
		} else if (context instanceof NumArgumentContext) {
			token = ((NumArgumentContext) context).NUM().getSymbol();
			if (!numAllowed) {
				// Num arguments are not allowed
				notifyErrorListeners("A num argument is not allowed.");
			} else {
				// TODO get value
			}
		} else if (context instanceof SeqArgumentContext) {
			if (!seqDefinitionAllowed) {
				// Seq definition arguments are not allowed
				notifyErrorListeners("A sequence definition argument is not allowed.");
			} else {
				new SeqDefinition(parser, ((SeqArgumentContext) context).seq_def());
				// TODO get value
			}
		} else if (context instanceof ParArgumentContext) {
			// Check sub argument
			Argument argument = new Argument(parser, ((ParArgumentContext) context).argument());
			argument.checkArgument(allowedVariableTypes, numAllowed, seqDefinitionAllowed, exprAllowed);
		} else {
			if (!exprAllowed) {
				// Expr arguments are not allowed
				notifyErrorListeners("An expression argument is not allowed.");
			} else {
				new Expr(parser, ((ExprArgumentContext) context).expr());
				// TODO get value
			}
		}
	}

	public VariableTypes determineType() {
		VariableTypes type = VariableTypes.var;
		if (context instanceof VarArgumentContext) {
			token = ((VarArgumentContext) context).ID().getSymbol();

			Variable variable = resolveVariable(((VarArgumentContext) context).ID());
			if (variable != null) {
				type = variable.getType();
			}
		} else if (context instanceof NumArgumentContext) {
			type = VariableTypes.num;
		} else if (context instanceof SeqArgumentContext) {
			type = VariableTypes.seq;
		} else if (context instanceof ParArgumentContext) {
			Argument argument = new Argument(parser, ((ParArgumentContext) context).argument());
			type = argument.determineType();
		}
		return type;
	}

}
