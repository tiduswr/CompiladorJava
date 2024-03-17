package com.uepb.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uepb.lexer.Lexer;
import com.uepb.parser.exceptions.SyntaxError;
import com.uepb.token.Token;
import com.uepb.token.TokenType;

public class Parser {
    
    private static final int BUFFER_SIZE = 10;
    private List<Token> buffer;
    Lexer lexer;

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        buffer = new ArrayList<>();
        confirmToken();
    }

    private void confirmToken() throws IOException {
    
        if(buffer.size() > 0) buffer.remove(0);
        
        while(buffer.size() < BUFFER_SIZE){
            var next = lexer.readNextToken();
            buffer.add(next);

            if(next.type() == TokenType.EOF) 
                break;
        }
    
    }

    private Token lookAhead(int k) {
        if(buffer.isEmpty()) return null;

        return k-1 >= buffer.size() ? buffer.get(buffer.size()-1) : buffer.get(k-1);
    }

    private void printMatchToken(Token t){
        PrintUtils.printCurrentMethod();
        PrintUtils.printlnGreen("✔ Match " + t + "!");
        if(t.type() == TokenType.SEMICOLON) 
            System.out.println();
    }

    private void match(TokenType type){
        var la = lookAhead(1);

        if(la.type() == type){
            printMatchToken(la);

            try{
                confirmToken();
            }catch(IOException ex){
                System.out.println(ex.getLocalizedMessage());
            }

        }else{
            throw new SyntaxError(la, type);
        }
    }

    public void parse(){
        prog();
    }

    // prog: listaComandos EOF;
    private void prog(){
        listaComandos();
        match(TokenType.EOF);
    }
    
    // listaComandos: comando*;
    private void listaComandos(){
        final var la = lookAhead(1).type();
        final List<TokenType> tokens = List.of(
            TokenType.PC_VAR,
            TokenType.IDENTIFIER,
            TokenType.PC_IF,
            TokenType.PC_WHILE,
            TokenType.PC_PRINT,
            TokenType.PC_INPUT,
            TokenType.PC_BREAK
        );

        if(tokens.contains(la)){
            comando();  
            listaComandos();
        }
    }

    // comando: (declaracao | atribuicao | if-decl | while | imprimir) ';';
    private void comando(){
        final var la = lookAhead(1).type();

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
        }else if(la == TokenType.PC_INPUT){
            ask_func();
        }else if(la == TokenType.PC_BREAK){
            break_decl();
        }else{
            throw new SyntaxError(lookAhead(1), TokenType.PC_VAR, TokenType.IDENTIFIER,
                TokenType.PC_IF, TokenType.PC_WHILE, TokenType.PC_PRINT);
        }


        match(TokenType.SEMICOLON);
    }

    // tipo: 'int' | 'float' | 'string';
    private void tipo(){
        final var la = lookAhead(1).type();

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

    // expr_arit:   expr_arit + expr_arit |
    //              expr_arit - expr_arit |
    //              termoArit;
    //
    // Refatorando
    //
    // expr_arit: expr_arit ('+' termoArit | 
    //                       '-' termoArit)
    //            | termoArit;
    //
    // expr_arit: termoArit expr_arit2;
    //    
    private void expr_arit(){
        termoArit(); 
        expr_arit2();
    }


    // expr_arit2: expr_arit_subRegra1 expr_arit2 | NULL
    private void expr_arit2(){
        final var la = lookAhead(1).type();

        if(la == TokenType.OP_SUM || la == TokenType.OP_SUBTRACT){
            expr_arit_subRegra1(); 
            expr_arit2();
        }
    }

    // expr_arit_subRegra1: ( '+' termoArit | '-' termoArit )
    private void expr_arit_subRegra1(){
        final var la = lookAhead(1).type();

        if(la == TokenType.OP_SUM){
            match(TokenType.OP_SUM); 
            termoArit();
        }else if(la == TokenType.OP_SUBTRACT){
            match(TokenType.OP_SUBTRACT); 
            termoArit();
        }else{
            throw new SyntaxError(lookAhead(1), TokenType.OP_SUBTRACT, TokenType.OP_SUM);
        }
    }

    // termoArit:   termoArit '*' termoArit |
    //              termoArit '/' termoArit |
    //              fatorArit;
    //
    // Refatorando
    //
    // termoArit: termoArit ('*' termoArit | 
    //                       '/' termoArit)
    //            | fatorArit;
    //
    // termoArit: fatorArit termoArit2;
    private void termoArit(){
        fatorArit(); 
        termoArit2();
    }

    //termoArit2: termoArit2_subRegra1 termoArit2 | NULL
    private void termoArit2(){
        final var la = lookAhead(1).type();

        if(la == TokenType.OP_MULTIPLY || la == TokenType.OP_DIVISION){
            termoArit2_subRegra1(); 
            termoArit2();
        }
    }
    // termoArit2_subRegra1: ('*' fatorArit | '/' fatorArit)
    private void termoArit2_subRegra1(){
        final var la = lookAhead(1).type();

        if(la == TokenType.OP_MULTIPLY){
            match(TokenType.OP_MULTIPLY); 
            fatorArit();
        }else if(la == TokenType.OP_DIVISION){
            match(TokenType.OP_DIVISION); 
            fatorArit();
        }else{
            throw new SyntaxError(lookAhead(1), TokenType.OP_MULTIPLY, TokenType.OP_DIVISION);
        }
    }

    // fatorArit: sinal (IDENTIFICADOR | INTEIRO | FLOAT | '(' expr_arit ')');
    private void fatorArit(){
        sinal();


        final var la = lookAhead(1).type();
        if(la == TokenType.IDENTIFIER){
            match(TokenType.IDENTIFIER);
        }else if(la == TokenType.INT){
            match(TokenType.INT);
        }else if(la == TokenType.FLOAT){
            match(TokenType.FLOAT);
        }else if(la == TokenType.OPEN_PAREN){
            match(TokenType.OPEN_PAREN); 
            expr_arit(); 
            match(TokenType.CLOSE_PAREN);
        }else{
            throw new SyntaxError(lookAhead(1), TokenType.INT, TokenType.IDENTIFIER,
                TokenType.FLOAT, TokenType.OPEN_PAREN);
        }
    }

    // sinal: '-' | '+' | NULL
    private void sinal(){
        final var la = lookAhead(1).type();

        if(la == TokenType.OP_SUBTRACT){
            match(TokenType.OP_SUBTRACT);
        }else if(la == TokenType.OP_SUM){
            match(TokenType.OP_SUBTRACT);
        }
    }
    
    // valor: expr_arit | STRING;
    private void valor(){
        final var la = lookAhead(1).type();

        if(la == TokenType.STRING){
            match(TokenType.STRING);
        }else{
            expr_arit();
        }
    }
    
    // declaracao: 'var' IDENTIFICADOR ':' tipo declaracaoAtribuicao;
    private void declaracao(){
        match(TokenType.PC_VAR); 
        match(TokenType.IDENTIFIER); 
        match(TokenType.COLON); 
        tipo(); 
        declaracaoAtribuicao();
    }

    // declaracaoAtribuicao: '=' valor | NULL;
    private void declaracaoAtribuicao(){
        final var la = lookAhead(1).type();

        if(la == TokenType.OP_ASSIGNMENT){
            match(TokenType.OP_ASSIGNMENT); 
            valor();
        }
    }

    // atribuicao: IDENTIFICADOR '=' valor;
    private void atribuicao(){
        final var la = lookAhead(1).type();

        if(la == TokenType.IDENTIFIER){
            match(TokenType.IDENTIFIER); 
            match(TokenType.OP_ASSIGNMENT); 
            valor();
        }else{
            throw new SyntaxError(lookAhead(1), TokenType.IDENTIFIER);
        }
    }

    // expr_rel: expr_rel op_bool termo_rel | termo_rel;
    // expr_rel: termo_rel expr_rel2;
    private void expr_rel(){
        termo_rel();
        expr_rel2();
    }

    // op_bool: 'and' | 'or'
    private void op_bool(){
        final var la = lookAhead(1).type();

        if(la == TokenType.OP_AND){
            match(TokenType.OP_AND);
        }else if(la == TokenType.OP_OR){
            match(TokenType.OP_OR);
        }else{
            throw new SyntaxError(lookAhead(1), TokenType.OP_AND, TokenType.OP_OR);
        }
    }

    // expr_rel2: op_bool termo_rel expr_rel2 | NULL
    private void expr_rel2(){
        final var la = lookAhead(1).type();
        
        if(la == TokenType.OP_AND || la == TokenType.OP_OR){
            op_bool(); 
            termo_rel();
            expr_rel2();
        }
    }

    //termo_rel: expr_arit OP_REL expr_arit | '(' expr_arit ')'
    private void termo_rel(){
        expr_arit();
        op_rel();
        expr_arit();
    }

    //op_rel: '==' | '!=' | '<' | '>' | '<=' | '>='
    private void op_rel(){
        final var la = lookAhead(1).type();
        final TokenType[] tokens = {
            TokenType.OP_EQUALS,
            TokenType.OP_NOT_EQUAL,
            TokenType.OP_LOWER_THAN,
            TokenType.OP_GREATER_THAN,
            TokenType.OP_LOWER_OR_EQUAL,
            TokenType.OP_GREATER_OR_EQUAL
        };

        if(la == tokens[0]){
            match(tokens[0]);
        } else if(la == tokens[1]){
            match(tokens[1]);
        } else if(la == tokens[2]){
            match(tokens[2]);
        } else if(la == tokens[3]){
            match(tokens[3]);
        } else if(la == tokens[4]){
            match(tokens[4]);
        } else if(la == tokens[5]){
            match(tokens[5]);
        } else {
            throw new SyntaxError(lookAhead(1), tokens);
        }
    }
    
    // if-decl: 'unless' '(' expr_rel ')' '{' listaComandos '}' ifTail;
    private void if_decl(){
        match(TokenType.PC_IF);
        match(TokenType.OPEN_PAREN);
        expr_rel();
        match(TokenType.CLOSE_PAREN);
        match(TokenType.OPEN_BRACE);
        listaComandos();
        match(TokenType.CLOSE_BRACE);
        ifTail();
    }

    // ifTail: ( 'do' '{' listaComandos '}' )?
    private void ifTail(){
        final var la = lookAhead(1).type();

        if(la == TokenType.PC_ELSE){
            match(TokenType.PC_ELSE); 
            match(TokenType.OPEN_BRACE); 
            listaComandos(); 
            match(TokenType.CLOSE_BRACE);
        }
    }
    
    // while: 'during' '(' expr_rel ')' '{' listaComandos '}';
    private void while_decl(){
        match(TokenType.PC_WHILE); 
        match(TokenType.OPEN_PAREN);
        expr_rel();
        match(TokenType.CLOSE_PAREN);
        match(TokenType.OPEN_BRACE);
        listaComandos();
        match(TokenType.CLOSE_BRACE);
    }
    
    // imprimir: 'show' '(' valor ')';
    private void print_func(){
        match(TokenType.PC_PRINT);
        match(TokenType.OPEN_PAREN);
        valor();
        match(TokenType.CLOSE_PAREN);
    }

    // input: 'ask' '(' ')'
    private void ask_func(){
        match(TokenType.PC_INPUT);
        match(TokenType.OPEN_PAREN);
        match(TokenType.CLOSE_PAREN);
    }
    
    // break: 'break'
    private void break_decl() {
        match(TokenType.PC_BREAK);
    }

}