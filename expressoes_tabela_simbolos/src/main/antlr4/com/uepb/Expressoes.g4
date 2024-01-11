grammar Expressoes;

programa
:
	expr
;

expr:
	TERMO=termo (OP1 TERMOS+=termo)*
;

termo:
	FATOR=fator (OP2 FATORES+=fator)*
;

fator: 
	'(' EXPR_PARENTESES=expr ')'
	| VAR=ID
	| CONST=NUM
	| 'let' listaDeclaracoes '->' SUB_EXPR=expr
;

listaDeclaracoes:
	declaracao (',' declaracao)*
;

declaracao:
	NOME=ID '=' expr
;

NUM: [0-9]+ ('.' [0-9]+)?;
OP1: '+' | '-';
OP2: '*' | '/';
ID: ID_START (ID_START | [0-9])*;
ID_START: ([a-z] | [A-Z] | '_');
WS: [ \t\r\n]+ -> skip;