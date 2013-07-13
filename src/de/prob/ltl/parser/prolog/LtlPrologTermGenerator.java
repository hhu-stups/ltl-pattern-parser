package de.prob.ltl.parser.prolog;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.semantic.Node;
import de.prob.ltl.parser.symboltable.SymbolTableManager;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;

public class LtlPrologTermGenerator {

	private final LtlParser parser;
	private final IPrologTermOutput pto;
	private final String currentStateID;
	private final ProBParserBase specParser;

	private final SymbolTableManager symbolTableManager;

	public LtlPrologTermGenerator(LtlParser parser, IPrologTermOutput pto, String currentStateID, ProBParserBase parserBase) {
		this.parser = parser;
		this.pto = pto;
		this.currentStateID = currentStateID;
		this.specParser = parserBase;

		symbolTableManager = parser.getSymbolTableManager();
	}

	public void generatePrologTerm() {
		for (Node node : symbolTableManager.getNodes()) {
			node.createPrologTerm(parser, pto, currentStateID, specParser);
		}
	}

}
