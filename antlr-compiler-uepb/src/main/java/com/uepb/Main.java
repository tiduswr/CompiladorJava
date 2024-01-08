package com.uepb;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {
    public static void main(String[] args) throws IOException {
        CharStream charStream = CharStreams.fromFileName(args[0]);
        UEPBLanguageLexer lexer = new UEPBLanguageLexer(charStream);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        UEPBLanguageParser parser = new UEPBLanguageParser(tokens);
        
        parser.programa();
    }
}