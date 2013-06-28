package de.prob.ltl.parser.symboltable;

import org.antlr.v4.runtime.ParserRuleContext;

public class Variable implements Symbol {

	public enum VariableTypes {
		var,
		num
	}

	private String name;
	private VariableTypes type;
	private ParserRuleContext oldValueContext;
	private ParserRuleContext valueContext;

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

	public ParserRuleContext getValueContext() {
		return valueContext;
	}

	public ParserRuleContext getOldValueContext() {
		return oldValueContext;
	}

	public void setValueContext(ParserRuleContext valueContext) {
		oldValueContext = this.valueContext;
		this.valueContext = valueContext;
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
