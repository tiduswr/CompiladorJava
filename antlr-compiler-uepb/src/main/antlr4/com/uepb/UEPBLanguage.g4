grammar UEPBLanguage;

programa: listaComandos EOF;

listaComandos: comando*;

comando:
        declaracao ';'
        | atribuicao ';'
        | ifDecl        
        | whileDecl     
        | print ';'
        | ask ';'
        ;

exprArit: termoArit (OP_ARIT_1 termoArit)*;
termoArit: fatorArit (OP_ARIT_2 fatorArit)*;
fatorArit: OP_ARIT_1? (ID | NUM_INT | NUM_REAL | '(' exprArit ')');

exprRel: termoRel (OP_BOOL outrosTermos+=termoRel)*;
termoRel:(v1=valor OP_REL v2=valor) | '(' exprRel ')';

valor: exprArit | cast;

declaracao:'spawn' ID ':' TIPO_VAR ('=' valor)?;

atribuicao: ID '=' valor;

ifDecl: 'unless' '(' exprRel ')' escopoIf=escopo ('do' escopoElse=escopo)?;

whileDecl: 'during' '(' exprRel ')' escopoWhile;

whileBreak: 'stop' ';';

escopoWhile: '{' (whileBreak | comando)* '}';

escopo: '{' listaComandos '}';

print: 'show' (valor | STRING);

ask: 'ask' ID;

cast: '(' TIPO_VAR ')' valor;

PAL_CHAVE: 'spawn'
        | 'unless'
        | 'do'
        | 'during'
        | 'show'
        | 'ask'
        | 'stop'
        | 'else'
        | 'if'
        ;

TIPO_VAR: 'int' | 'float';

OP_BOOL: 'and' | 'or';

NUM_INT: [0-9]+;

NUM_REAL: [0-9]+ '.' [0-9]+;

ID: [a-z_] [a-z0-9_]*;

fragment ESCAPE : '\\' .;
STRING: '"' (ESCAPE | ~[\n"])* '"';

OP_REL: '>' | '>=' | '<' | '<=' | '==' | '!=';

OP_ARIT_1: '+' | '-';

OP_ARIT_2: '*' | '/';

ABRE_PAR: '(';

FECHA_PAR: ')';

ABRE_CHAVE: '{';

FECHA_CHAVE: '}';

DELIMITADOR: ':';

VIRGULA: ',';

PONTO_VIRGULA: ';';

COMENTARIO: '#' ~( '\r' | '\n' )* -> skip;

WS: ( ' ' | '\t' | '\r' | '\n' ) -> skip;