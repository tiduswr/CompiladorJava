package com.uepb;

import java.io.IOException;

import com.uepb.lexer.LexicalAnalyzer;
import com.uepb.token.Token;
import com.uepb.token.TokenType;

public class Main {
    public static void main(String[] args) throws IOException {
        
        LexicalAnalyzer lexer = new LexicalAnalyzer(args[0], true);

        Token token;
        while ((token = lexer.readNextToken()).type != TokenType.EOF) {
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