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

        if (args.length < 1) {
            System.err.println("Uso: java -jar pcode.jar <arquivo_pcode> [debugMode]");
            System.err.println("  <arquivo_pcode>  - Caminho para o arquivo contendo o código P-code.");
            System.err.println("  [debugMode]      - (Opcional) true para habilitar o modo de depuração, false para desabilitar (padrão: true).");
            return;
        }

        boolean debugMode = true;
        if (args.length > 1) {
            debugMode = Boolean.parseBoolean(args[1]);
        }

        StringBuilder program = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(args[0]);
             InputStreamReader reader = new InputStreamReader(fis);
             BufferedReader buffer = new BufferedReader(reader)) {

            String line;
            while ((line = buffer.readLine()) != null) {
                program.append(line).append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        PCodeInterpreter interpreter = new PCodeInterpreter(MEMORY_ADRESS_SIZE);
        interpreter.reset(program.toString());

        if (debugMode) System.out.println("### Início do Programa:");

        while (!interpreter.isHalted()) {
            if (debugMode) {
                System.out.println("\n#### " + interpreter.getCurrentInstructionInString());
                System.out.println("Pilha: " + interpreter.getStackDescription());
                System.out.println("Memória: " + interpreter.getMemoryDescription());
            }

            String result = interpreter.step();

            if (interpreter.isWaitingForInput()) {
                if(debugMode) System.out.println();
                System.out.print("[Input]: ");
                String input = scanner.nextLine();
                interpreter.setInput(input);
            } else {
                if (result != null && !interpreter.isHalted()) {
                    if(debugMode) System.out.println();
                    System.out.println("[Output]: " + result);
                }
            }

            if (debugMode) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (debugMode) System.out.println("\n### Fim do Programa:");
        scanner.close();
    }
}