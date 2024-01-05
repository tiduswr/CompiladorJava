lexer grammar UEPBLexer;

PalChave: 'var'
        | 'int'
        | 'float'
        | 'string'
        | 'if'
        | 'while'
        | 'print'
        | 'break';

NumInt: ('+' | '-')? ('0'..'9')+;

NumReal: ('+' | '-')? ('0'..'9')+ '.' ('0'..'9')+;

Ident: ('a'..'z') ('a'..'z' | 'A'..'Z' | '0'..'9')*;

String: '\'' (SeqEsc | ~('\'' | '\\') )* '\'';

fragment
SeqEsc: '\\\'';

OpRel: '>' | '>=' | '<' | '<=' | '==' | '!=' | '=' | '!';

OpArt: '+' | '-' | '*' | '/';

AbrePar: '(';

FechaPar: ')';

AbreChave: '{';

FechaChave: '}';

Delim: ':';

Virgula: ',';

PontoVirgula: ';';

EspacoBranco: ( ' ' | '\t' | '\r' | '\n' ) -> skip;