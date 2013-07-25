package de.prob.ltl.parser.prolog.scope;

import de.prob.prolog.term.CompoundPrologTerm;
import de.prob.prolog.term.PrologTerm;

public class BeforeScopeReplacer extends ScopeReplacer {

	@Override
	public PrologTerm scopeFormula(PrologTerm term) {
		PrologTerm f = new CompoundPrologTerm("finally", getR());
		return new CompoundPrologTerm("implies", f, term);
	}

	@Override
	public PrologTerm globally(PrologTerm a) {
		// a U r
		return new CompoundPrologTerm("until", a, getR());
	}

	@Override
	public PrologTerm finallyOp(PrologTerm a) {
		// !r U (a & !r)
		PrologTerm notR = new CompoundPrologTerm("not", getR());
		return new CompoundPrologTerm("until", notR, new CompoundPrologTerm("and", a, notR));
	}

	@Override
	public PrologTerm next(PrologTerm a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrologTerm historically(PrologTerm a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrologTerm once(PrologTerm a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrologTerm yesterday(PrologTerm a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrologTerm until(PrologTerm a, PrologTerm b) {
		// (a & !r) U (b & !r)
		PrologTerm notR = new CompoundPrologTerm("not", getR());
		return new CompoundPrologTerm("until", new CompoundPrologTerm("and", a, notR), new CompoundPrologTerm("and", b, notR));
	}

	@Override
	public PrologTerm weakuntil(PrologTerm a, PrologTerm b) {
		// a U (r | b)
		return new CompoundPrologTerm("until", a, new CompoundPrologTerm("or", getR(), b));
	}

	@Override
	public PrologTerm release(PrologTerm a, PrologTerm b) {
		// b U (r | (a & b))
		return new CompoundPrologTerm("until", b, new CompoundPrologTerm("or", getR(), new CompoundPrologTerm("and", a, b)));
	}

	@Override
	public PrologTerm since(PrologTerm a, PrologTerm b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrologTerm trigger(PrologTerm a, PrologTerm b) {
		// TODO Auto-generated method stub
		return null;
	}

}
