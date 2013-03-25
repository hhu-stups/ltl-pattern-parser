grammar Ltl;

@header {
package de.prob.ltl.parser;
}

/* -- Rules -- */
start 		: expression;

expression	: LEFT_PAREN expression RIGHT_PAREN												# parenthesisExpression
			| NOT expression																# notExpression
			| expression AND expression														# andExpression	
			| expression OR expression														# orExpression	
			| expression IMPLIES expression													# impliesExpression		
			| expression binary_op=(UNTIL | WEAKUNTIL | RELEASE | SINCE | TRIGGER) expression		# binaryExpression			  
			| unary_op=(GLOBALLY | FINALLY | NEXT | HISTORICALLY | ONCE | YESTERDAY | UNARY_COMBINED) expression		# unaryExpression
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

// Unary operators
NOT				: ('not' | '!'); 
GLOBALLY		: ('G');
FINALLY			: ('F');
NEXT			: ('X');
HISTORICALLY	: ('H');
ONCE			: ('O');
YESTERDAY		: ('Y');
UNARY_COMBINED 	: [GFXHOY]+;

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

// Enabled
ENABLED			: 'e' ENABLED_PAREN;
fragment 
ENABLED_PAREN	: LEFT_PAREN (~('(' | ')') | ENABLED_PAREN)* RIGHT_PAREN;

// Others
LEFT_PAREN		: '(';
RIGHT_PAREN		: ')';

IDENTIFIER		: [a-zA-Z]+;
WS				: [ \t\r\n]+ -> skip;