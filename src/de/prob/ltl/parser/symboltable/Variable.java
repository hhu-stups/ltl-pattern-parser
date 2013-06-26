package de.prob.ltl.parser.symboltable;

public class Variable implements Symbol {

	public enum VariableTypes {
		var,
		num
	}

	private String name;
	private VariableTypes type;

	public Variable(String name, VariableTypes type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public VariableTypes getType() {
		return type;
	}

	public void setType(VariableTypes type) {
		this.type = type;
	}

	@Override
	public String getSymbolID() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, type.name());
	}

}
