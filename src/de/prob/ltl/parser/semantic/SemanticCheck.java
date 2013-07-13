package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.LoopContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;
import de.prob.ltl.parser.LtlParser.StartContext;
import de.prob.ltl.parser.LtlParser.Var_assignContext;
import de.prob.ltl.parser.LtlParser.Var_defContext;

public class SemanticCheck {

	private LtlParser parser;

	public SemanticCheck(LtlParser parser) {
		this.parser = parser;
	}

	public void check(StartContext ast) {
		// Collect all pattern definitions and check them
		collectAndCheckPatternDefinitions(ast);

		// Check in global scope:
		for (ParseTree child : ast.children) {
			if (child instanceof Var_defContext) {
				// Define variable and check its initial value
				/*VariableDefinition definition = */
				new VariableDefinition(parser, (Var_defContext) child);
			} else if (child instanceof Var_assignContext) {
				// Check variable and its assigned value
				/*VariableAssignment assignment = */
				new VariableAssignment(parser, (Var_assignContext) child);
			} else if (child instanceof LoopContext) {
				// Check loop with its count variable, arguments and body
				/*Loop loop = */
				new Loop(parser, (LoopContext) child);
			}
		}
		// Check final expr
		/* ExprOrAtomCheck formulaStart = */
		new ExprOrAtomCheck(parser, ast.expr());
	}

	private void collectAndCheckPatternDefinitions(StartContext ast) {
		List<PatternDefinition> patternDefinitions = new LinkedList<PatternDefinition>();
		// Collect and define all pattern definitions
		for (ParseTree child : ast.children) {
			if (child instanceof Pattern_defContext) {
				patternDefinitions.add(new PatternDefinition(parser, (Pattern_defContext) child));
			}
		}
		// Check pattern definitions after all patterns are defined
		for (PatternDefinition definition : patternDefinitions) {
			definition.checkBody();
		}
	}

}
