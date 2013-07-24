package de.prob.ltl.parser.prolog.scope;

import de.prob.ltl.parser.semantic.ScopeCall;
import de.prob.ltl.parser.semantic.Variable;
import de.prob.prolog.output.IPrologTermOutput;
import de.prob.prolog.term.CompoundPrologTerm;
import de.prob.prolog.term.PrologTerm;

public class BeforeScopePrologTermGenerator {

	private final IPrologTermOutput pto;

	public BeforeScopePrologTermGenerator(IPrologTermOutput pto) {
		this.pto = pto;
	}

	public void generatePrologTerm(ScopeCall call) {
		Variable rArg = call.getArguments().get(0);
		Variable pArg = call.getArguments().get(1);

		PrologTerm r = rArg.getValue();
		PrologTerm p = replace(r, pArg.getValue());

		pto.openTerm("implies");
		pto.openTerm("finally");
		pto.printTerm(r);
		pto.closeTerm();
		pto.printTerm(p);
		pto.closeTerm();
	}

	private PrologTerm replace(PrologTerm r, PrologTerm term) {
		if (term.getFunctor().equals("ap") || term.getFunctor().equals("action") || term.isAtom()) {
			return term;
		}
		PrologTerm result = term;

		PrologTerm p = replace(r, term.getArgument(1));	// Note: Indexing starts with 1
		PrologTerm notR = new CompoundPrologTerm("not", r);

		if (term.getArity() == 1) {
			if (term.getFunctor().equals("globally")) {
				// p U r
				result = new CompoundPrologTerm("until", p, r);
			} else if (term.getFunctor().equals("finally")) {
				// !r U (p & !r)
				result = new CompoundPrologTerm("until", notR, new CompoundPrologTerm("and", p, notR));
			} else {
				// DEFAULT: Just replace the argument
				result = new CompoundPrologTerm(term.getFunctor(), p);
			}
		} else if (term.getArity() == 2) {
			PrologTerm q = replace(r, term.getArgument(2));	// Note: Indexing starts with 1
			if (term.getFunctor().equals("until")) {
				// (p & !r) U (q & !r)
				result = new CompoundPrologTerm("until", new CompoundPrologTerm("and", p, notR), new CompoundPrologTerm("and", q, notR));
			} else if (term.getFunctor().equals("release")) {
				// q U (r | (q & p))
				result = new CompoundPrologTerm("until", q, new CompoundPrologTerm("or", r, new CompoundPrologTerm("and", q, p)));
			} else if (term.getFunctor().equals("weakuntil")) {
				// p U (r | q)
				result = new CompoundPrologTerm("until", p, new CompoundPrologTerm("or", r, q));
			} else {
				// DEFAULT: Just replace the arguments
				result = new CompoundPrologTerm(term.getFunctor(), p, q);
			}
		}

		return result;
	}

}
