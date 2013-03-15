grammar Ltl;

@lexer::header {
package de.prob.ltl.parser;
}

@parser::header {
package de.prob.ltl.parser;
import de.prob.parserbase.ProBParserBase;
}

@parser::members {
private ProBParserBase parserBase;

public LtlParser(TokenStream input, ProBParserBase parserBase) {
	this(input);
	this.parserBase = parserBase;
}

public ProBParserBase getParserBase() {
	return parserBase;
}

public void setParserBase(ProBParserBase parserBase) {
	this.parserBase = parserBase;
}
}
/* -- Token -- */

// Constants
TRUE			: 'true';
FALSE			: 'false';
SINK			: 'sink';
DEADLOCK		: 'deadlock';
CURRENT			: 'current';

// Unary operators
NOT				: ('not' | '!'); 
GLOBALLY		: ('G');
FINALLY			: ('F');
NEXT			: ('X');
HISTORICALLY	: ('H');
ONCE			: ('O');
YESTERDAY		: ('Y');

// Binary operators
AND				: ('and' | '&');
OR				: ('or' | '|');
IMPLIES			: ('=>');
UNTIL			: ('U');
WEAKUNTIL		: ('W');
RELEASE			: ('R');
SINCE			: ('S');
TRIGGER			: ('T');

// Predicates
LEFT_CURLY		: '{';
RIGHT_CURLY		: '}';
PREDICATE		: LEFT_CURLY (~('{' | '}') | PREDICATE)* RIGHT_CURLY;

// Actions / Transition predicates
LEFT_BRACKET	: '[';
RIGHT_BRACKET	: ']';
ACTION			: LEFT_BRACKET (~('[' | ']') | ACTION)* RIGHT_BRACKET;

// Others
LEFT_PAREN		: '(';
RIGHT_PAREN		: ')';
WS				: [ \t\r\n]+ -> skip;

/* -- Rules -- */
start 		: expression;

expression	: expression  binary_op expression	# binaryExpression			  
			| unary_op expression				# unaryExpression
			| LEFT_PAREN expression RIGHT_PAREN	# parenthesisExpression
			| PREDICATE							# predicateExpression 
			| ACTION							# actionExpression
			| constant							# constantExpression
			;
			
unary_op	: NOT 
			| GLOBALLY 
			| FINALLY 
			| NEXT 
			| HISTORICALLY 
			| ONCE 
			| YESTERDAY;
			
binary_op	: AND 
			| OR 
			| IMPLIES 
			| UNTIL 
			| WEAKUNTIL 
			| RELEASE 
			| SINCE 
			| TRIGGER;

constant	: TRUE 
			| FALSE 
			| SINK 
			| DEADLOCK 
			| CURRENT;
