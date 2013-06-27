package de.prob.ltl.parser.symboltable;

import java.util.LinkedList;
import java.util.List;

public class Loop extends Scope {

	public enum LoopTypes {
		up,
		down
	}

	private LoopTypes type = LoopTypes.up;
	private List<Variable> parameters = new LinkedList<Variable>();

	public Loop(Scope parent, LoopTypes type) {
		super(parent);
		this.type = type;
	}

	public LoopTypes getType() {
		return type;
	}

	public List<Variable> getParameters() {
		return parameters;
	}

	public void addParameter(Variable parameter) {
		parameters.add(parameter);
	}

	@Override
	public String toString() {
		return String.format("loop %s", type);
	}

}
