grammar Ltl;

@header {
package de.prob.ltl.parser;
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

// Others
LEFT_PAREN		: '(';
RIGHT_PAREN		: ')';
WS				: [ \t\r\n]+ -> skip;

/* -- Rules -- */
start 		: expression;

expression	: expression  binary_op expression	# binaryExpression			  
			| unary_op expression				# unaryExpression
			| LEFT_PAREN expression RIGHT_PAREN	# parenthesisExpression
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
