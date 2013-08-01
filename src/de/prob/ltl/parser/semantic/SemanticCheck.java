package de.prob.ltl.parser.semantic;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.BodyContext;
import de.prob.ltl.parser.LtlParser.Pattern_defContext;

public class SemanticCheck {

	private final LtlParser parser;

	public SemanticCheck(LtlParser parser) {
		this.parser = parser;
	}

	public void check(BodyContext ast) {
		// Collect all pattern definitions and check them
		collectAndCheckPatternDefinitions(ast);

		// Check body
		/*Body body = */new Body(parser, ast);
	}

	private void collectAndCheckPatternDefinitions(BodyContext ast) {
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
