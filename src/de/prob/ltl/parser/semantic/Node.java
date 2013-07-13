package de.prob.ltl.parser.semantic;

import de.prob.ltl.parser.LtlParser;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;

public interface Node {

	void createPrologTerm(LtlParser parser, IPrologTermOutput pto, String currentState, ProBParserBase parserBase);

}
