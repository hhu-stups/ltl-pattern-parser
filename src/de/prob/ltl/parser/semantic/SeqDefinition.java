package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.SeqValueDefinitionContext;
import de.prob.ltl.parser.LtlParser.SeqValueIDContext;
import de.prob.ltl.parser.LtlParser.Seq_valueContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class SeqDefinition {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private Seq_valueContext context;
	private Token token;
	private boolean isDefinition;
	private List<Variable> parameters = new LinkedList<Variable>();

	public SeqDefinition(LtlParser parser, Seq_valueContext context) {
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			determineTokenAndType();
			// TODO check arguments
			checkArguments();
		}
	}
	/*
	seq_value
	 : LEFT_PAREN var_value (',' var_value)+ (SEQ_WITHOUT var_value)? RIGHT_PAREN	# seqValueDefinition
	 | ID SEQ_WITHOUT var_value														# seqValueID
	 ;
	 */
	private void determineTokenAndType() {
		TerminalNode node = null;
		if (context instanceof SeqValueDefinitionContext) {
			isDefinition = true;
			// TODO create token from starttoken and stoptoken
			node = ((SeqValueDefinitionContext) context).LEFT_PAREN();

		} else {
			isDefinition = false;
			// TODO create token from starttoken and stoptoken
			node = ((SeqValueIDContext) context).ID();
		}
		token = node.getSymbol();
	}

	private void checkArguments() {
		if (isDefinition) {
			SeqValueDefinitionContext ctx = (SeqValueDefinitionContext) context;
			for (Var_valueContext var_value : ctx.var_value()) {
				VariableValue value = new VariableValue(parser, var_value);
				if (value.getValueType().equals(VariableTypes.num)){
					parser.notifyErrorListeners(token, String.format("Type mismatch. Type '%s' is not allowed here.", VariableTypes.num), null);
				}
			}
		} else {
			SeqValueIDContext ctx = (SeqValueIDContext) context;
			TerminalNode node = ctx.ID();
			String name = node.getText();

			Variable variable = symbolTableManager.resolveVariable(name);
			if (variable == null) {
				parser.notifyErrorListeners(token, String.format("Variable '%s' cannot be resolved.", name), null);
			} else if (!variable.getType().equals(VariableTypes.seq)){
				parser.notifyErrorListeners(token, String.format("Type mismatch. Expected variable type '%s'. Found variable '%s'.", VariableTypes.seq, variable), null);
			}
		}
	}

	// Getters
	public Seq_valueContext getContext() {
		return context;
	}

	public Token getToken() {
		return token;
	}

	public List<Variable> getParameters() {
		return parameters;
	}

}
