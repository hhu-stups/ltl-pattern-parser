package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.NumVarParamContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.LtlParser.Pattern_def_paramContext;
import de.prob.ltl.parser.LtlParser.SeqVarParamContext;
import de.prob.ltl.parser.LtlParser.VarParamContext;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.symboltable.Variable;
import de.prob.ltl.parser.symboltable.VariableTypes;

public class PatternDefinition extends AbstractSemanticObject {

	private final SymbolTable symbolTable;

	private Pattern_defContext context;

	private String name;
	private List<Variable> parameters = new LinkedList<Variable>();

	public PatternDefinition(LtlParser parser, Pattern_defContext context) {
		super(parser);
		symbolTable = new SymbolTable(symbolTableManager.getCurrentScope());

		this.context = context;
		if (this.context != null) {
			symbolTableManager.pushScope(symbolTable);
			determineTokenAndName();
			determineParameters();
			symbolTableManager.popScope();

			// Define pattern
			if (!symbolTableManager.define(this)) {
				notifyErrorListeners("The pattern '%s' is already defined.", getName());
			}
		}
	}

	private void determineTokenAndName() {
		TerminalNode node = context.ID();
		token = node.getSymbol();
		name = node.getText();
	}

	private void determineParameters() {
		for (Pattern_def_paramContext ctx : context.pattern_def_param()) {
			Variable parameter = null;
			if (ctx instanceof NumVarParamContext) {
				parameter = createVariable(((NumVarParamContext) ctx).ID(), VariableTypes.num);
			} else if (ctx instanceof SeqVarParamContext) {
				parameter = createVariable(((SeqVarParamContext) ctx).ID(), VariableTypes.seq);
			} else {
				parameter = createVariable(((VarParamContext) ctx).ID(), VariableTypes.var);
			}

			defineVariable(parameter);
			parameters.add(parameter);
		}
	}

	public void checkBody() {
		symbolTableManager.pushScope(symbolTable);
		/*Body body = */new Body(parser, context.body());
		symbolTableManager.popScope();
	}

	public String getName() {
		return createPatternIdentifier(name, parameters);
	}

	public List<Variable> getParameters() {
		return parameters;
	}

	private static void printParam(StringBuilder sb, VariableTypes type, int count) {
		sb.append(type);
		if (count > 1) {
			sb.append(count);
		}
	}

	public static String createPatternIdentifier(String name, List<Variable> parameters) {
		StringBuilder sb = new StringBuilder(name);
		sb.append('/');
		if (parameters.size() == 0) {
			sb.append(0);
		} else {
			VariableTypes lastType = parameters.get(0).getType();
			int count = 1;
			for (int i = 1; i < parameters.size(); i++) {
				VariableTypes current = parameters.get(i).getType();
				if (lastType.equals(current)) {
					count++;
				} else {
					printParam(sb, lastType, count);
					lastType = current;
					count = 1;
				}
			}
			printParam(sb, lastType, count);
		}
		return sb.toString();
	}

}
