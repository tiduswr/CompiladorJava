package com.uepb.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class PCodeInterpreter {
    private LinkedList<String> stack;
    private String[] memory;
    private String input;
    private List<String> instructions;
    private int currentInstruction;
    private boolean halt;
    private boolean waitForInput;
    private Map<String, Integer> labelIndexMap;
    private final int MEMORY_ADRESS_SIZE;

    public PCodeInterpreter(int MEMORY_ADRESS_SIZE) {
        this.MEMORY_ADRESS_SIZE = MEMORY_ADRESS_SIZE;
        stack = new LinkedList<>();
        labelIndexMap = new HashMap<>();
    }

    public void reset(String program) {
        StringTokenizer stProgram = new StringTokenizer(program, "\n\r");
        instructions = new ArrayList<>();
        int index = 0;

        while (stProgram.hasMoreTokens()) {
            String instruction = stProgram.nextToken();
            instructions.add(instruction);

            if (instruction.startsWith("lab")) {
                String label = getInstructionValue(instruction);
                labelIndexMap.put(label, index);
            }

            index++;
        }

        stack.clear();
        memory = new String[MEMORY_ADRESS_SIZE];
        currentInstruction = 0;
        halt = false;
        waitForInput = false;
    }

    public String getCurrentInstructionInString() {
        return currentInstruction < instructions.size() ? instructions.get(currentInstruction) : null;
    }

    public void setInput(String input) {
        this.input = (input != null && !input.trim().isEmpty()) ? input : null;
    }

    public boolean isHalted() {
        return halt;
    }

    public boolean isWaitingForInput() {
        return waitForInput;
    }

    public String step() {
        waitForInput = false;

        if (halt)
            return null;

        String instruction = getCurrentInstructionInString();

        if(instruction == null)
            return null;

        String instructionValue = getInstructionValue(instruction);

        currentInstruction++;
        switch (instruction.substring(0, 3)) {
            case "rdi":
                handleRdi(instructionValue);
                break;
            case "wri":
                return handleWri();
            case "lda":
                stack.push(instructionValue);
                break;
            case "ldc":
                stack.push(instructionValue);
                break;
            case "lod":
                int i = Integer.parseInt(instructionValue);
                if (i >= MEMORY_ADRESS_SIZE)
                    throw new RuntimeException("O endereço de memória " + i + " é inválido");
                stack.push(memory[i]);
                break;
            case "mpi":
                handleBinaryOperation("*");
                break;
            case "dvi":
                handleBinaryOperation("/");
                break;
            case "adi":
                handleBinaryOperation("+");
                break;
            case "sbi":
                handleBinaryOperation("-");
                break;
            case "sto":
                handleSto();
                break;
            case "grt":
                handleComparison(">");
                break;
            case "let":
                handleComparison("<");
                break;
            case "gte":
                handleComparison(">=");
                break;
            case "lte":
                handleComparison("<=");
                break;
            case "equ":
                handleComparison("==");
                break;
            case "neq":
                handleComparison("!=");
                break;
            case "and":
                handleLogicalOperation("&&");
                break;
            case "or":
                handleLogicalOperation("||");
                break;
            case "toi":
                handleConversion("int");
                break;
            case "tof":
                handleConversion("float");
                break;
            case "lab":
                // do nothing
                break;
            case "ujp":
                jumpTo(instructionValue);
                break;
            case "fjp":
                handleFalseJump(instructionValue);
                break;
            case "stp":
                halt = true;
                return "Fim da execução!";
        }
        return null;
    }

    private String pop() {
        if (stack.isEmpty())
            throw new RuntimeException("A pilha está vazia!");
        return stack.pop();
    }

    private void handleRdi(String instructionValue) {
        if (input == null) {
            currentInstruction--;
            waitForInput = true;
        } else {
            stack.push(input);
            input = null;
        }
    }

    private String handleWri() {
        var test = pop();
        return test;
    }

    private void handleBinaryOperation(String operator) {

        final String t1 = pop();
        final String t2 = pop();

        Number left = GenericCalculator.parseNumber(t1);
        Number right = GenericCalculator.parseNumber(t2);

        String result = "";
        switch (operator) {
            case "+":
                result = GenericCalculator.sum(left, right);
                break;
            case "-":
                result = GenericCalculator.sub(right, left);
                break;
            case "*":
                result = GenericCalculator.mult(left, right);
                break;
            case "/":
                result = GenericCalculator.div(right, left);
                break;
            default:
                break;
        }

        stack.push(result);
    }

    private void handleConversion(String to) {
        final String stackTop = pop();

        if (to.equals("int")) {
            int top = GenericCalculator.parseNumber(stackTop).intValue();
            stack.push(Integer.toString(top));
        } else if (to.equals("float")) {
            float top = GenericCalculator.parseNumber(stackTop).floatValue();
            stack.push(Float.toString(top));
        }
    }

    private void handleSto() {
        String valueToStore = pop();
        int addressToStore = Integer.parseInt(pop());
        memory[addressToStore] = valueToStore;
    }

    private void handleComparison(String operator) {
        int op1 = Integer.parseInt(pop());
        int op2 = Integer.parseInt(pop());
        boolean result = false;
        switch (operator) {
            case ">":
                result = op2 > op1;
                break;
            case "<":
                result = op2 < op1;
                break;
            case ">=":
                result = op2 >= op1;
                break;
            case "<=":
                result = op2 <= op1;
                break;
            case "==":
                result = op2 == op1;
                break;
            case "!=":
                result = op2 != op1;
                break;
        }
        stack.push(Boolean.toString(result));
    }

    private void handleLogicalOperation(String operator) {
        boolean op1 = Boolean.parseBoolean(pop());
        boolean op2 = Boolean.parseBoolean(pop());
        boolean result = false;
        switch (operator) {
            case "&&":
                result = op1 && op2;
                break;
            case "||":
                result = op1 || op2;
                break;
        }
        stack.push(Boolean.toString(result));
    }

    private void jumpTo(String label) {
        if (labelIndexMap.containsKey(label)) {
            currentInstruction = labelIndexMap.get(label);
        }
    }

    private void handleFalseJump(String label) {
        boolean bValue = Boolean.parseBoolean(pop());
        if (!bValue) {
            jumpTo(label);
        }
    }

    private String getInstructionValue(String instruction) {
        return (instruction.length() >= 4) ? instruction.substring(4) : null;
    }

    public String getStackDescription() {
        var retorno = String.join(" < ", stack);
        return retorno.isBlank() ? "" : " < " + String.join(" < ", stack);
    }

    public String getMemoryDescription() {
        List<String> memoryToString = new ArrayList<>();
        var nonNullMemoryLocation = Stream.of(memory)
                .filter(e -> e != null)
                .toList();

        for (int i = 0; i < nonNullMemoryLocation.size(); i++) {
            var curMemLoc = nonNullMemoryLocation.get(i);
            memoryToString.add(String.format("[%d]:", i) + curMemLoc);
        }

        var retorno = String.join(" > ", memoryToString);
        return retorno.isBlank() ? "" : " > " + String.join(" > ", memoryToString);
    }
}