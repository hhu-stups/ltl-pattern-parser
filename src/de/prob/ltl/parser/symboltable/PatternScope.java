package de.prob.ltl.parser.symboltable;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.LtlParser.Pattern_scopeContext;

public enum PatternScope {
	global,
	before,
	after,
	between,
	until;

	public static PatternScope determine(Pattern_scopeContext ctx) {
		if (ctx != null) {
			switch(ctx.scope.getType()) {
			case LtlParser.BEFORE_SCOPE:
				return before;
			case LtlParser.AFTER_SCOPE:
				return after;
			case LtlParser.BETWEEN_SCOPE:
				return between;
			case LtlParser.UNTIL_SCOPE:
				return until;
			}
		}
		return global;
	}

}
