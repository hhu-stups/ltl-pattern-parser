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

// Others
LEFT_PAREN		: '(';
RIGHT_PAREN		: ')';
WS				: [ \t\r\n]+ -> skip;

/* -- Rules -- */
start 		: expression;

expression	: unary_op=NOT expression								# unaryExpression
			| LEFT_PAREN expression RIGHT_PAREN						# parenthesisExpression
			| constant												# constantExpression
			;

constant	: (TRUE | FALSE | SINK | DEADLOCK | CURRENT);