package com.uepb;

import java.io.IOException;

import com.uepb.lexer.LexicalAnalyzer;
import com.uepb.lexer.Token;
import com.uepb.lexer.TokenType;

public class Main {
    public static void main(String[] args) throws IOException {
        
        LexicalAnalyzer lexer = new LexicalAnalyzer(args[0], false);

        Token token;
        while ((token = lexer.readNextToken()) != null) {
            if(token.type == TokenType.LINE_BREAK){
                System.err.println();
            }else if(token.type == TokenType.WHITE_SPACE){
                System.out.print(' ');
            }else{
                System.out.print(token);
            }
        }
        
    }
}