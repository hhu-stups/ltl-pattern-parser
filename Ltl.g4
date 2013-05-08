grammar Ltl;

@lexer::header {
package de.prob.ltl.parser;
}

@lexer::members {
public static String[] getKeywords() {
	return new String[] {
		"def",
		"true",
		"false",
		"sink",
		"deadlock",
		"current",
		"true"
	};
}

public static String[] getBooleanOperators() {
	return new String[] {
		"and",
		"or",
		"not"
	};
}

public static String[] getBooleanOperatorsSymbols() {
	return new String[] {
		"&",
		"|",
		"!",
		"=>"
	};
}
}

@parser::header {
package de.prob.ltl.parser;

import de.prob.ltl.parser.symbolcheck.SymbolChecker;
import de.prob.ltl.parser.symbolcheck.SymbolCollector;
import de.prob.ltl.parser.symboltable.Symbol;
import de.prob.ltl.parser.symboltable.SymbolTable;
import de.prob.ltl.parser.warning.WarningListener;
}

@parser::members {
private List<WarningListener> warningListeners = new ArrayList<WarningListener>();

public void addWarningListener(WarningListener listener) {
	warningListeners.add(listener);
}

public void removeWarningListener(WarningListener listener) {
	warningListeners.remove(listener);
}

public void removeAllWarningListeners() {
	warningListeners.clear();
}

public void notifyWarningListeners(String message, Symbol ... symbols) {
	for (WarningListener listener : warningListeners) {
		listener.warning(message, symbols);
	}
}

public void semanticCheck(ParseTree ast) {
	SymbolTable symbolTable = new SymbolTable(this);
	ParseTreeWalker walker = new ParseTreeWalker();
	walker.walk(new SymbolCollector(symbolTable), ast);
	walker.walk(new SymbolChecker(symbolTable), ast);
}
}

/* -- Rules -- */
start 		: pattern_def* expression pattern_def* EOF;

pattern_def	: 'def' PATTERN_ID LEFT_PAREN PATTERN_ID (',' PATTERN_ID)? RIGHT_PAREN ':' expression;

expression	: LEFT_PAREN expression RIGHT_PAREN												# parenthesisExpression
			| NOT expression																# notExpression
			| unary_op=(GLOBALLY | FINALLY | NEXT | HISTORICALLY | ONCE | YESTERDAY | UNARY_COMBINED) expression		# unaryExpression
			| expression binary_op=(UNTIL | WEAKUNTIL | RELEASE | SINCE | TRIGGER) expression		# binaryExpression			  
			| expression AND expression														# andExpression	
			| expression OR expression														# orExpression	
			| expression IMPLIES expression													# impliesExpression		
			| PATTERN_ID 															# patternVarExpression
			| PATTERN_ID LEFT_PAREN expression (',' expression)* RIGHT_PAREN 		# patternCallExpression
			| PREDICATE																		# predicateExpression 
			| ACTION																		# actionExpression
			| ENABLED																		# enabledExpression
			| constant																		# constantExpression
			;
			

constant	: TRUE 
			| FALSE 
			| SINK 
			| DEADLOCK 
			| CURRENT;

/* -- Token -- */

// Constants
TRUE			: 'true';
FALSE			: 'false';
SINK			: 'sink';
DEADLOCK		: 'deadlock';
CURRENT			: 'current';

// Unary Ltl operators
GLOBALLY		: ('G');
FINALLY			: ('F');
NEXT			: ('X');
HISTORICALLY	: ('H');
ONCE			: ('O');
YESTERDAY		: ('Y');
UNARY_COMBINED 	: [GFXHOY]+;

// Binary Ltl operators
UNTIL			: ('U');
WEAKUNTIL		: ('W');
RELEASE			: ('R');
SINCE			: ('S');
TRIGGER			: ('T');

// Boolean operators
NOT				: ('not' | '!'); 
AND				: ('and' | '&');
OR				: ('or' | '|');
IMPLIES			: ('=>');

// Predicates
LEFT_CURLY		: '{';
RIGHT_CURLY		: '}';
PREDICATE		: LEFT_CURLY (~('{' | '}') | PREDICATE)* RIGHT_CURLY;

// Actions / Transition predicates
LEFT_BRACKET	: '[';
RIGHT_BRACKET	: ']';
ACTION			: LEFT_BRACKET (~('[' | ']') | ACTION)* RIGHT_BRACKET;

// Enabled
ENABLED			: 'e' ENABLED_PAREN;
fragment 
ENABLED_PAREN	: LEFT_PAREN (~('(' | ')') | ENABLED_PAREN)* RIGHT_PAREN;

// Others
LEFT_PAREN		: '(';
RIGHT_PAREN		: ')';
			
COMMENT			: ('//' ~('\n')* 
				| '/*' .*? '*/') -> skip;

PATTERN_ID		: [a-zA-Z] [a-zA-Z0-9_]*;
WS				: [ \t\r\n]+ -> skip;