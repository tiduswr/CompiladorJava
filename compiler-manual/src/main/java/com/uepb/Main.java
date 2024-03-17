package com.uepb;

import java.io.IOException;

import com.uepb.lexer.Lexer;
import com.uepb.parser.Parser;

public class Main {
    public static void main(String[] args) throws IOException {
        
        Lexer lexer = new Lexer("code_example.uepb", true);
        Parser parser = new Parser(lexer);
        parser.parse();
        
    }
}