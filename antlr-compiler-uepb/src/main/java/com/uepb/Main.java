package com.uepb;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Main {
    public static void main(String[] args) throws IOException {

        CharStream cs = CharStreams.fromFileName(args[0]);

        UEPBLexer lexer = new UEPBLexer(cs);

        Token t;
        while((t = lexer.nextToken()).getType() != Token.EOF){
            var type = UEPBLexer.VOCABULARY.getSymbolicName(t.getType());
            System.out.println("<"+type+","+t.getText()+">");
        }

    }
}