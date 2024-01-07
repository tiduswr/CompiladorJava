package com.uepb;

import java.io.IOException;

import com.uepb.lexer.Lexer;
import com.uepb.parser.Parser;

public class Main {
    public static void main(String[] args) throws IOException {
        
        Lexer lexer = new Lexer(args[0], true);

        // Token token;
        // while ((token = lexer.readNextToken()).type() != TokenType.EOF) {
        //     // if(token.type() == TokenType.LINE_BREAK){
        //     //     System.err.println();
        //     // }else if(token.type() == TokenType.WHITE_SPACE){
        //     //     System.out.print(' ');
        //     // }else{
        //     //     System.out.print(token);
        //     // }
        //     System.out.println(token);
        // }

        Parser parser = new Parser(lexer);
        parser.parse();
        
    }
}