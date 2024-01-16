package com.uepb;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.uepb.interpreter.PCodeInterpreter;

public class Main {

    public static final int MEMORY_ADRESS_SIZE = 50;

    public static void main(String[] args) {

        if(args.length < 1)
            throw new RuntimeException("É esperado um arquivo com código p-code no arg[1]");
        
        StringBuilder program = new StringBuilder();
        try(FileInputStream fis = new FileInputStream(args[0])){
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader buffer = new BufferedReader(reader);

            String line;
            while((line = buffer.readLine()) != null){
                program.append(line + "\n");
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        PCodeInterpreter interpreter = new PCodeInterpreter(MEMORY_ADRESS_SIZE);
        interpreter.reset(program.toString());

        System.out.println("### Início do Programa:");

        while (!interpreter.isHalted()) {
            System.out.println("\n#### " + interpreter.getCurrentInstructionInString());
            System.out.println("Pilha: " + interpreter.getStackDescription());
            System.out.println("Memória: " + interpreter.getMemoryDescription());

            String result = interpreter.step();

            if (interpreter.isWaitingForInput()) {
                System.out.print("\n[OUTPUT] - Aguardando Input: ");
                String input = scanner.nextLine();
                interpreter.setInput(input);
            } else {
                if (result != null && !interpreter.isHalted()) {
                    System.out.println("\n[OUTPUT] - Resultado: " + result);
                }
            }

            // Aguarda um pequeno intervalo para melhor visualização durante a execução
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n### Fim do Programa:");
        scanner.close();
    }
}
