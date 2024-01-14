package com.uepb;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.uepb.geradores.CodeBuilderC;
import com.uepb.semantic.Semantic;
import com.uepb.semantic.Utils;

public class Main {
    public static void main(String[] args) throws IOException {
        CharStream charStream = CharStreams.fromFileName(args[0]);
        UEPBLanguageLexer lexer = new UEPBLanguageLexer(charStream);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        UEPBLanguageParser parser = new UEPBLanguageParser(tokens);
        
        var tree = parser.programa();

        Semantic semantic = new Semantic(true);
        semantic.visitPrograma(tree);

        System.out.println("\n\n######### RELATÓRIO DE ERROS SEMÂNTICOS\n");
        Utils.semanticErrors.forEach(System.out::println);

        System.out.println("\n\n######### Código gerado\n");
        var builder = new CodeBuilderC();
        builder.visitPrograma(tree);
        System.out.println(builder.getGeneratedCode());
    }
}