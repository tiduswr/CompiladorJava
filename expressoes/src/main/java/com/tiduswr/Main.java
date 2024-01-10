package com.tiduswr;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

public class Main {

    public static void main(String[] args) throws IOException {
        CharStream charStream = CharStreams.fromFileName(args[0]);
        ExpressoesLexer lexer = new ExpressoesLexer(charStream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressoesParser parser = new ExpressoesParser(tokenStream);
        var arvore = parser.programa();
        Calculador calculador = new Calculador();
        var valor = calculador.visitPrograma(arvore);

        System.out.println("Valor do programa.expr: " + valor);
    }
}
