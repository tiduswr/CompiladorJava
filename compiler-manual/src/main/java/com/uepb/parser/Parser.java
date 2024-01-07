package com.uepb.parser;

import java.util.ArrayList;
import java.util.List;

import com.uepb.lexer.LexicalAnalyzer;
import com.uepb.parser.exceptions.SyntaxError;
import com.uepb.token.Token;
import com.uepb.token.TokenType;

public class Parser {
    
    private static final int BUFFER_SIZE = 10;
    private List<Token> buffer;
    LexicalAnalyzer lexer;
    boolean eof = false;

    public Parser(LexicalAnalyzer lexer){
        this.lexer = lexer;
        buffer = new ArrayList<>();
        confirmToken();
    }

    private void confirmToken(){
    
        if(buffer.size() > 0) buffer.remove(0);
        
        while(buffer.size() < BUFFER_SIZE && !eof){
            var next = lexer.readNextToken();

            buffer.add(next);

            if(next.type() == TokenType.EOF){
                eof = true;
            }

        }
        System.out.println("Lido: " + lookAhead(1));
    
    }

    private Token lookAhead(int k) {
        if(buffer.isEmpty()) return null;
        return k-1 >= buffer.size() ? buffer.get(buffer.size()-1) : buffer.get(k-1);
    }

    private void match(TokenType type){
        if(lookAhead(1).type() == type){
            System.out.println("Match: " + lookAhead(1));
            confirmToken();
        }else{
            throw new SyntaxError(lookAhead(1), type);
        }
    }

    // prog: listaComandos;
    public void prog(){
        listaComandos();
    }

    // listaComandos: (comando ';')*;
    public void listaComandos(){
        if(!(lookAhead(1).type() == TokenType.EOF)){
            comando(); match(TokenType.SEMICOLON); listaComandos();
        } 
    }

    // comando: declaracao | atribuicao | if-decl | while | imprimir;
    public void comando(){
        var la = lookAhead(1).type();

        if(la == TokenType.PC_VAR){
            declaracao();
        }else if(la == TokenType.IDENTIFIER){
            atribuicao();
        }else if(la == TokenType.PC_IF){
            if_decl();
        }else if(la == TokenType.PC_WHILE){
            while_decl();
        }else if(la == TokenType.PC_PRINT){
            print_func();
        }else{
            throw new SyntaxError(lookAhead(1), TokenType.PC_VAR, TokenType.IDENTIFIER,
                TokenType.PC_IF, TokenType.PC_WHILE, TokenType.PC_PRINT);
        }
    }

    // tipo: 'int' | 'float' | 'string';
    public void tipo(){
        var la = lookAhead(1).type();

        if(la == TokenType.PC_INT){
            match(TokenType.PC_INT);
        }else if(la == TokenType.PC_FLOAT){
            match(TokenType.PC_FLOAT);
        }else if(la == TokenType.PC_STRING){
            match(TokenType.PC_STRING);
        }else{
            throw new SyntaxError(lookAhead(1), TokenType.PC_INT, 
                TokenType.PC_FLOAT, TokenType.PC_STRING);
        }
        
    }

    // expr_arit: termo (('+' | '-') termo)*;
    public void expr_arit(){
        var la = lookAhead(1).type();



    }

    // termo: fator (('*' | '/') fator)*;
    public void termo(){

    }

    // fator: IDENTIFICADOR | NUMERO | '(' expr_arit ')' | STRING;
    public void fator(){

    }
    
    // valor: expr_arit | STRING;
    public void valor(){

    }
    
    // declaracao: 'var' IDENTIFICADOR ':' tipo |
    //             'var' IDENTIFICADOR ':' tipo '=' valor;
    public void declaracao(){

    }
    
    // atribuicao: IDENTIFICADOR '=' valor;
    public void atribuicao(){

    }
    
    // expr_rel: expr_arit ('==' | '!=' | '<' | '>' | '<=' | '>=') expr_arit;
    public void expr_rel(){

    }
    
    // if-decl: 'if' '(' expr_rel ')' '{' listaComandos '}' ( 'else' '{' listaComandos '}' )?;
    public void if_decl(){

    }
    
    // while: 'while' '(' expr_rel ')' '{' listaComandos '}';
    public void while_decl(){

    }
    
    // imprimir: 'print' '(' IDENTIFICADOR | STRING ')';
    public void print_func(){

    }
    
}
