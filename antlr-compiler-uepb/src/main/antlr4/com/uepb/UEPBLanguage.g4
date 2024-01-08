grammar UEPBLanguage;

// ********************* TOKENS

PalChave: 'spawn'
        | 'unless'
        | 'do'
        | 'during'
        | 'show'
        | 'ask'
        | 'stop';

TipoVar: 'int' | 'float' | 'string';

OpBool: 'and' | 'or';

NumInt: ('0'..'9')+;

NumReal: ('0'..'9')+ '.' ('0'..'9')+;

Ident: ('a'..'z') ('a'..'z' | 'A'..'Z' | '0'..'9')*;

String: '"' (SeqEsc | ~('"' | '\\') )* '"';

fragment
SeqEsc: '\\"';

OpRel: '>' | '>=' | '<' | '<=' | '==' | '!=' | '=' | '!';

OpArt1: '+' | '-';

OpArt2: '*' | '/';

AbrePar: '(';

FechaPar: ')';

AbreChave: '{';

FechaChave: '}';

Delim: ':';

Virgula: ',';

PontoVirgula: ';';

Comentario: '//' ~('\n' | '\r') '\r'? '\n' -> skip;

EspacoBranco: ( ' ' | '\t' | '\r' | '\n' ) -> skip;

// ********************* GRAMÁTICA

// prog: listaComandos;
programa: { System.out.println("Começou um programa!"); } 
        lc=listaComandos EOF { 
                System.out.println("Número de comandos: " + $lc.numComandos);
                System.out.println("Último Comando: " + $lc.ultimoComando);
};

// listaComandos: (comando ';')+;
listaComandos returns [int numComandos, String ultimoComando]
@init {
        $numComandos = 0; 
        $ultimoComando = "";
}
: (comando {
        System.out.println("Comando do tipo: " + $comando.tipoComando);
        $numComandos++;
        $ultimoComando = $comando.text;
} )+;

// comando: declaracao | atribuicao | if-decl | while | imprimir;
comando returns [String tipoComando]: (
        declaracao      { $tipoComando = "Declaração"; }
        | atribuicao    { $tipoComando = "Atribuição"; }
        | ifDecl        { $tipoComando = "Estrutura unless"; }
        | whileDecl     { $tipoComando = "Estrutura during"; }
        | printFunc     { $tipoComando = "Função show"; }
        | askFunc       { $tipoComando = "Função input"; }
        | 'stop'        { $tipoComando = "stop"; }
) ';';

// expr_arit: expr_arit ('+' termoArit | 
//                       '-' termoArit)
//            | termoArit;
exprArit: termoArit (OpArt1 termoArit)*;

// termoArit: termoArit ('*' termoArit | 
//                       '/' termoArit)
//            | fatorArit;
termoArit: fatorArit (OpArt2 fatorArit)*;

// fatorArit: IDENTIFICADOR | INTEIRO | FLOAT | '(' expr_arit ')';
fatorArit: OpArt1? (Ident | NumInt | NumReal  | '(' exprArit ')');

// valor: expr_arit | STRING;
valor: exprArit | String;

// declaracao: 'var' IDENTIFICADOR ':' tipo declaracaoAtribuicao;
declaracao:'spawn' Ident ':' TipoVar ('=' valor)? { 
                System.out.println("Declaracao: Var=" + $Ident.text + ", Tipo=" + $TipoVar.text); 
        };

// atribuicao: IDENTIFICADOR '=' valor;
atribuicao: Ident '=' valor { 
                System.out.println("Atribuição: Var=" + $Ident.text + ", Valor=" + $valor.text); 
        };

// expr_rel: expr_rel op_bool termo_rel | termo_rel;
exprRel: termoRel (OpBool termoRel)*;

//termo_rel: expr_arit OP_REL expr_arit | '(' expr_arit ')'
termoRel:(valor OpRel valor) | '(' exprRel ')';

// if-decl: 'unless' '(' expr_rel ')' '{' listaComandos '}' ifTail;
ifDecl: 'unless' '(' exprRel ')' '{' unless=listaComandos? '}' ('do' '{' unless_do=listaComandos? '}' )? { 
        int numComandos = $unless.numComandos;
        String ultimoComando = $unless.ultimoComando;

        if($unless_do.text != null){
                numComandos = numComandos + $unless_do.numComandos;
                ultimoComando = $unless_do.ultimoComando;
        }

        System.out.println("Número de comandos(unless): " + numComandos);
        System.out.println("Último Comando(unless): " + ultimoComando);
};

// while: 'during' '(' expr_rel ')' '{' listaComandos '}';
whileDecl: 'during' '(' exprRel ')' '{' listaComandos? '}';

// imprimir: 'show' '(' valor ')';
printFunc: 'show' '(' valor ')';

// input: 'ask' '(' valor ')';
askFunc: 'ask' '(' ')';