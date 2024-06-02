package com.uepb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java Programa <nome_do_arquivo> <modo>");
            System.out.println("Modos:");
            System.out.println("  calcular  - Calcula o valor da expressão");
            System.out.println("  pcode     - Gera o código P-code");
            return;
        }
    
        String filename = args[0];
        String mode = args[1];
    
        CharStream charStream = CharStreams.fromFileName(filename);
        ExpressoesLexer lexer = new ExpressoesLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressoesParser parser = new ExpressoesParser(tokenStream);
        ParseTree tree = parser.programa();
    
        if (mode.equals("calcular")) {
            Calculadora calculadora = new Calculadora();
            var valor = calculadora.visitPrograma((ExpressoesParser.ProgramaContext) tree);
            System.out.println("Valor do cálculo: " + valor);
        } else if (mode.equals("pcode")) {
            CalculadoraPcode generator = new CalculadoraPcode();
            List<String> pcode = generator.generate(tree);
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.pcode"))) {
                for (var instruction : pcode) {
                    writer.write(instruction);
                    writer.newLine();
                }
            }
        } else {
            System.out.println("Modo desconhecido: " + mode);
            System.out.println("Modos disponíveis: calcular, pcode");
        }
    }    
}
