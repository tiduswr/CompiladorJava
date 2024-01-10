grammar Expr;

programa returns [int val]
: 
	expressao EOF {$val = $expressao.val}
;

expressao returns [int val]
: 
	termo expressao2[$termo.val] {$val = $expressao2.sintetizado}
;

expressao2[int herdado] returns [int sintetizado]
: 
	'+' termo expressao2[$termo.val + herdado] {$sintetizado = $expressao2.sintetizado}
	| '-' termo expressao2[herdado - $termo.val] {$sintetizado = $expressao2.sintetizado}
	| {$sintetizado = $herdado;}
;

termo returns [int val]
: 
	fator termo2[$fator.val] {$val = $termo2.sintetizado;}
;

termo2[int herdado] returns [int sintetizado]
: 
	'*' fator termo2[$fator.val * herdado] {$sintetizado = $termo2.sintetizado} 
	| '/' fator termo2[herdado / $fator.val] {$sintetizado = $termo2.sintetizado} 
	| {$sintetizado = $herdado;}
;

fator returns [int val]
: 	
	'(' expressao ')' {$val= $expressao.val}
	| NUM {$val = int($NUM.text)}
;

NUM: '0'..'9';
WS: (' ' | '\n' | '\r' | '\t') -> skip;