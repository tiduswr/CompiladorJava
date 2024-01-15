package com.uepb;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.uepb.geradores.CodeBuilderC;
import com.uepb.semantic.Semantic;
import com.uepb.semantic.Utils;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length < 2)
            throw new RuntimeException("É precisa passar dois paramêtros ([1] - codigo, [2] - arquivo_saida)");
        
        CharStream charStream = CharStreams.fromFileName(args[0]);
        UEPBLanguageLexer lexer = new UEPBLanguageLexer(charStream);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        UEPBLanguageParser parser = new UEPBLanguageParser(tokens);
        var tree = parser.programa();

        Semantic semantic = new Semantic(false);
        
        try{
            semantic.visitPrograma(tree);

            if(Utils.semanticErrors.isEmpty()){
                var builder = new CodeBuilderC();
                builder.visitPrograma(tree);
                try(PrintWriter writer = new PrintWriter(new File("./build/" + args[1] + ".c"))){
                    writer.print(builder.getGeneratedCode());
                }
            }else{
                System.out.println("\n\n######### RELATÓRIO DE ERROS SEMÂNTICOS\n");
                Utils.semanticErrors.forEach(System.out::println);
            }
        }catch(Exception ex){
            System.out.println("\nPor causa dos erros anteriores, não foi possível inicializar o analisador semântico corretamente.");
        }
    }
}