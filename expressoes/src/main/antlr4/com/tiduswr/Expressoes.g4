// grammar Expressoes;

// programa returns [int val]
// : 
// 	expressao EOF {$val = $expressao.val;}
// ;

// expressao returns [int val]
// : 
// 	termo expressao2[$termo.val] {$val = $expressao2.sintetizado;}
// ;

// expressao2[int herdado] returns [int sintetizado]
// : 
// 	'+' termo expressao2[$termo.val + herdado] {$sintetizado = $expressao2.sintetizado;}
// 	| '-' termo expressao2[herdado - $termo.val] {$sintetizado = $expressao2.sintetizado;}
// 	| { $sintetizado = $herdado; }
// ;

// termo returns [int val]
// : 
// 	fator termo2[$fator.val] {$val = $termo2.sintetizado;}
// ;

// termo2[int herdado] returns [int sintetizado]
// : 
// 	'*' fator termo2[$fator.val * herdado] {$sintetizado = $termo2.sintetizado;} 
// 	| '/' fator termo2[herdado / $fator.val] {$sintetizado = $termo2.sintetizado;} 
// 	| { $sintetizado = $herdado; }
// ;

// fator returns [int val]
// : 	
// 	'(' expressao ')' {$val= $expressao.val;}
// 	| NUM {$val = Integer.parseInt($NUM.text);}
// ;

// NUM: '0'..'9';
// WS: (' ' | '\n' | '\r' | '\t') -> skip;

// REFATORANDO GRAMÁTICA COM BASE NAS CAPACIDADES DO ANTLR
// Usando visitante do ANTLR é possível separar as análises semânticas
// da gramática, como abaixo é demonstrado de forma bem limpa no código:

grammar Expressoes;

programa:
	expressao EOF
;

expressao:
	t1=termo (op1 termos+=termo)*
;

termo: 
	f1=fator (op2 fatores+=fator)*
;

fator:
	CS_ABRE_PAR expressao CS_FECHA_PAR 
	| NUM
;

op1: 
	OP_SUM | OP_SUB
;

op2: 
	OP_DIV | OP_MUL
;

OP_SUM: [+];
OP_MUL: [*];
OP_DIV: [/];
OP_SUB: [-];
CS_ABRE_PAR: [(];
CS_FECHA_PAR: [)];
NUM: [0-9]+([.][0-9]+)?;
WS: [ \t\r\n] -> skip;
