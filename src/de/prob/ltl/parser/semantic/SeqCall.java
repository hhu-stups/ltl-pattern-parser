package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.SeqCallDefinitionContext;
import de.prob.ltl.parser.LtlParser.SeqCallSimpleContext;
import de.prob.ltl.parser.LtlParser.Seq_callContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.symboltable.VariableTypes;
import de.prob.prolog.term.PrologTerm;

public class SeqCall implements Node {

	private final LtlParser parser;

	private Seq_callContext context;
	private Token token;
	private boolean useWithout;
	private List<Variable> arguments = new LinkedList<Variable>();
	private List<VariableValue> argumentNodes = new LinkedList<VariableValue>();

	public SeqCall(LtlParser parser, Seq_callContext context) {
		this.parser = parser;
		this.context = context;

		if (this.context != null) {
			determineTokenAndType();
			determineArguments();

			// Check types of arguments and parameters
			checkArguments();
		}
	}
	private void determineTokenAndType() {
		TerminalNode node = null;
		if (context instanceof SeqCallSimpleContext) {
			node = ((SeqCallSimpleContext) context).SEQ_VAR();
		} else {
			node = ((SeqCallDefinitionContext) context).SEQ_VAR();
			useWithout = (((SeqCallDefinitionContext) context).SEQ_WITHOUT() != null);
		}
		token = node.getSymbol();
	}

	private void determineArguments() {
		if (context instanceof SeqCallSimpleContext) {
			VariableValue value = new VariableValue(parser, ((SeqCallSimpleContext) context).var_value());

			Variable argument = new Variable(null, value.getValueType());
			argument.setToken(value.getToken());
			addArgument(arguments, argument);
			argumentNodes.add(value);
		} else {
			for (Var_valueContext ctx : ((SeqCallDefinitionContext) context).var_value()) {
				VariableValue value = new VariableValue(parser, ctx);

				Variable argument = new Variable(null, value.getValueType());
				argument.setToken(value.getToken());
				addArgument(arguments, argument);
				argumentNodes.add(value);
			}
		}
	}

	/*
seq_call
 : SEQ_VAR LEFT_PAREN var_value RIGHT_PAREN												# seqCallSimple
 | SEQ_VAR LEFT_PAREN var_value (',' var_value)+ (SEQ_WITHOUT var_value)? RIGHT_PAREN	# seqCallDefinition
 ;
	 */
	private void checkArguments() {
		if (context instanceof SeqCallSimpleContext) {
			for (Variable argument : arguments) {
				checkArgumentType(VariableTypes.seq, argument);
			}
		} else {
			for (Variable argument : arguments) {
				if (argument.getType().equals(VariableTypes.num)){
					parser.notifyErrorListeners(token, String.format("Type mismatch. Type '%s' is not allowed here.", VariableTypes.num), null);
				}
			}
		}
	}

	private void checkArgumentType(VariableTypes expected, Variable argument) {
		if (!argument.getType().equals(expected)) {
			String msg = String.format("Type mismatch. Passed argument has the type '%s'. Expected type '%s'.", argument.getType(), expected);
			if (argument.getToken() != null) {
				parser.notifyErrorListeners(argument.getToken(), msg, null);
			} else {
				// TODO create token from starttoken and stoptoken
				parser.notifyErrorListeners(msg);
			}
		}
	}

	private void addArgument(List<Variable> argumentList, Variable var) {
		argumentList.add(var);
	}

	// Getters
	public Token getToken() {
		return token;
	}

	public boolean usesWithout() {
		return useWithout;
	}

	public List<Variable> getArguments() {
		return arguments;
	}

	public List<VariableValue> getArgumentNodes() {
		return argumentNodes;
	}

	public PrologTerm getValue(int index) {
		PrologTerm value = null;

		if (index < arguments.size()) {
			value = arguments.get(index).getValue();
		}

		return value;
	}

	@Override
	public String toString() {
		// TODO
		return String.format("seqcall()");
	}

}
