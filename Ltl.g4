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

// Binary operators
AND				: ('and' | '&');
OR				: ('or' | '|');
IMPLIES			: ('=>');

// Others
LEFT_PAREN		: '(';
RIGHT_PAREN		: ')';
WS				: [ \t\r\n]+ -> skip;

/* -- Rules -- */
start 		: expression;

expression	: expression  binary_op=(AND | OR | IMPLIES) expression	# binaryExpression			  
			| unary_op=NOT expression								# unaryExpression
			| LEFT_PAREN expression RIGHT_PAREN						# parenthesisExpression
			| constant												# constantExpression
			;

constant	: (TRUE | FALSE | SINK | DEADLOCK | CURRENT);