package de.prob.ltl.parser.prolog.scope;

import de.prob.ltl.parser.LtlParser;
import de.prob.ltl.parser.prolog.LtlPrologTermGenerator;
import de.prob.ltl.parser.semantic.ScopeCall;
import de.prob.ltl.parser.semantic.ScopeTypes;
import de.prob.parserbase.ProBParserBase;
import de.prob.prolog.output.IPrologTermOutput;

public class ScopePrologTermGenerator {

	private final LtlParser parser;
	private final LtlPrologTermGenerator generator;

	private final IPrologTermOutput pto;
	private final String currentStateID;
	private final ProBParserBase parserBase;

	public ScopePrologTermGenerator(LtlParser parser, LtlPrologTermGenerator generator, IPrologTermOutput pto, String currentStateID, ProBParserBase parserBase) {
		this.parser = parser;
		this.generator = generator;
		this.pto = pto;
		this.currentStateID = currentStateID;
		this.parserBase = parserBase;
	}

	public void generatePrologTerm(ScopeCall call) {
		if (call.getType().equals(ScopeTypes.BEFORE)) {
			BeforeScopePrologTermGenerator before = new BeforeScopePrologTermGenerator(pto);
			before.generatePrologTerm(call);
		}
	}

}
