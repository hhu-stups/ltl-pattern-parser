package de.prob.ltl.parser.semantic;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.LoopContext;
import de.prob.ltl.parser.LtlParser.Var_assignContext;
import de.prob.ltl.parser.LtlParser.Var_defContext;
import de.prob.ltl.parser.LtlParser.Var_valueContext;
import de.prob.ltl.parser.symboltable.LoopTypes;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class Loop extends SymbolTable {

	private final LtlParser parser;
	private final SymbolTableManager symbolTableManager;

	private LoopContext context;
	private Token token;
	private LoopTypes type;
	private Variable countVariable;

	public Loop(LtlParser parser, LoopContext context) {
		super(parser.getSymbolTableManager().getCurrentScope(), true);
		this.parser = parser;
		this.context = context;

		symbolTableManager = parser.getSymbolTableManager();

		if (this.context != null) {
			symbolTableManager.pushScope(this);

			determineLoopInfo();
			checkLoopArguments();
			checkLoopBody();

			symbolTableManager.popScope();
		}
	}

	private void determineLoopInfo() {
		TerminalNode beginNode = context.LOOP_BEGIN();
		token = beginNode.getSymbol();
		type = (context.UP() != null ? LoopTypes.up : LoopTypes.down);

		TerminalNode node = context.ID();
		if (node != null) {
			String name = node.getText();
			countVariable = new Variable(name, VariableTypes.num);

			// Define variable
			if (!symbolTableManager.define(countVariable)) {
				parser.notifyErrorListeners(node.getSymbol(), String.format("The variable '%s' is already defined.", name), null);
			}
		}
	}

	private void checkLoopArguments() {
		for (int i = 0; i < context.var_value().size(); i++) {
			Var_valueContext var_value = context.var_value(i);

			VariableValue value = new VariableValue(parser, var_value);
			VariableTypes valueType = value.getValueType();

			if (!valueType.equals(VariableTypes.num)) {
				String msg = String.format("Type mismatch. Loop argument has the type '%s'. Expected type '%s'.", valueType, VariableTypes.num);
				if (value.getToken() != null) {
					parser.notifyErrorListeners(value.getToken(), msg, null);
				} else {
					// TODO create token from starttoken and stoptoken
					parser.notifyErrorListeners(msg);
				}
			}
		}
	}

	private void checkLoopBody() {
		for (ParseTree child : context.loop_body().children) {
			if (child instanceof Var_defContext) {
				// Define variable and check its initial value
				/*VariableDefinition definition = */
				new VariableDefinition(parser, (Var_defContext) child);
			} else if (child instanceof Var_assignContext) {
				// Check variable and its assigned value
				/*VariableAssignment assignment = */
				new VariableAssignment(parser, (Var_assignContext) child);
			}
		}
	}

	// Getters
	public Token getToken() {
		return token;
	}

	public LoopTypes getLoopType() {
		return type;
	}

}
