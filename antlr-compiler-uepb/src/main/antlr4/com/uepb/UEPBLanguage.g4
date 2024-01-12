grammar UEPBLanguage;

programa: listaComandos EOF;

listaComandos: (comando)+;

comando: (
        declaracao      
        | atribuicao    
        | ifDecl        
        | whileDecl     
        | printFunc     
        | askFunc       
        | 'stop'        
) ';';

exprArit: termoArit (OP_ARIT_1 termoArit)*;

termoArit: fatorArit (OP_ARIT_2 fatorArit)*;

fatorArit: OP_ARIT_1? (ID | NUM_INT | NUM_REAL  | '(' exprArit ')');

valor: exprArit | STRING | askFunc;

declaracao:'spawn' ID ':' TIPO_VAR ('=' valor)?;

atribuicao: ID '=' valor;

exprRel: termoRel (OP_BOOL termoRel)*;

termoRel:(valor OP_REL valor) | '(' exprRel ')';

ifDecl: 'unless' '(' exprRel ')' escopo ifTail?;

ifTail:  'do' escopo;

whileDecl: 'during' '(' exprRel ')' escopo;

escopo: '{' listaComandos? '}';

printFunc: 'show' '(' valor ')';

askFunc: 'ask' '(' ')';

PAL_CHAVE: 'spawn'
        | 'unless'
        | 'do'
        | 'during'
        | 'show'
        | 'ask'
        | 'stop';

TIPO_VAR: 'int' | 'float' | 'string';

OP_BOOL: 'and' | 'or';

NUM_INT: ('0'..'9')+;

NUM_REAL: ('0'..'9')+ '.' ('0'..'9')+;

ID: ('a'..'z') ('a'..'z' | 'A'..'Z' | '0'..'9')*;

STRING: '"' (SeqEsc | ~('"' | '\\') )* '"';

fragment
SeqEsc: '\\"';

OP_REL: '>' | '>=' | '<' | '<=' | '==' | '!=' | '=' | '!';

OP_ARIT_1: '+' | '-';

OP_ARIT_2: '*' | '/';

ABRE_PAR: '(';

FECHA_PAR: ')';

ABRE_CHAVE: '{';

FECHA_CHAVE: '}';

DELIMITADOR: ':';

VIRGULA: ',';

PONTO_VIRGULA: ';';

COMENTARIO: '//' ~('\n' | '\r') '\r'? '\n' -> skip;

WS: ( ' ' | '\t' | '\r' | '\n' ) -> skip;