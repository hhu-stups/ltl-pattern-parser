package de.prob.ltl.parser.symboltable;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import de.prob.ltl.parser.LtlParser;

public class SymbolTable {

	private LtlParser parser;
	private final Scope globalScope = new Scope(null);
	private Stack<Scope> scopeStack = new Stack<Scope>();
	private ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();

	public SymbolTable(LtlParser parser) {
		this.parser = parser;
		scopeStack.push(globalScope);
	}

	public void define(Symbol symbol) {
		String name = symbol.getSymbolID();
		if (isDefined(symbol)) {
			boolean isPattern = symbol instanceof Pattern;

			Token token = symbol.getToken();
			String msg = String.format("%s '%s' is already defined.", (isPattern ? "Pattern" : "Variable"), name);
			if (token != null) {
				parser.notifyErrorListeners(token, msg, null);
			} else {
				parser.notifyErrorListeners(msg);
			}
		} else {
			try {
				getCurrentScope().define(symbol);
			} catch(RuntimeException e) {
				Token token = symbol.getToken();
				if (token != null) {
					parser.notifyErrorListeners(token, e.getMessage(), null);
				} else {
					parser.notifyErrorListeners(e.getMessage());
				}
			}
		}
	}

	public boolean isDefined(Symbol symbol) {
		return isDefined(symbol.getSymbolID());
	}

	public boolean isDefined(String name) {
		return resolve(name) != null;
	}

	public Symbol resolve(String name) {
		return getCurrentScope().resolve(name);
	}

	public List<Symbol> getSymbols() {
		return getCurrentScope().getSymbols();
	}

	public Scope getCurrentScope() {
		return scopeStack.peek();
	}

	public void pushScope(Scope scope, ParserRuleContext context) {
		scopes.put(context, scope);
		scope.setDefinitionContext(context);
		pushScope(scope);
	}

	public void pushScope(Scope scope) {
		if (!getCurrentScope().equals(scope)) {
			scopeStack.push(scope);
		}
	}

	public void pushScope(ParserRuleContext context) {
		Scope scope = scopes.get(context);
		pushScope(scope);
	}

	public void popScope() {
		if (scopeStack.size() > 1) {
			scopeStack.pop();
		}
	}

	public void checkTypes(Pattern call) {
		Pattern definedPattern = (Pattern) resolve(call.getSymbolID());
		definedPattern.checkParameterTypes(call.getParameters());
	}

}
