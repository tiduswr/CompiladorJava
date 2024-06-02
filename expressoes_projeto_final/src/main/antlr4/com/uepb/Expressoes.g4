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

expr_rel: 
    TERMO=termo_rel (OP_BOOL TERMOS+=termo_rel)*
;

termo_rel:
    (v1=fator_rel OP_REL v2=fator_rel) 
    | '(' expr_rel ')'
;

fator_rel:
    VAR=ID | CONST=NUM | '(' EXPR=expr ')'
;

fator: 
	'(' EXPR_PARENTESES=expr ')'
	| VAR=ID
	| CONST=NUM
	| 'let' listaDeclaracoes '->' SUB_EXPR=expr
	| expr_rel '?' IF_TRUE=expr (':' IF_FALSE=expr)?
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
OP_BOOL: 'and' | 'or';
OP_REL: '>' | '>=' | '<' | '<=' | '==' | '!=';
ID: ID_START (ID_START | [0-9])*;
ID_START: ([a-z] | [A-Z] | '_');
WS: [ \t\r\n]+ -> skip;