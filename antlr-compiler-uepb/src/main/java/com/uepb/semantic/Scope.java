package com.uepb.semantic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Scope {

    private int value = -1;
    private final LinkedList<SymbolTable> stack;
    private final boolean debugMode;

    public Scope(boolean debugMode) {
        stack = new LinkedList<>();
        this.debugMode = debugMode;
    }

    public void startScope() {
        value++;
        if(debugMode) 
            System.out.println(String.join("", Collections.nCopies(value, " ")) 
                + "Escopo Aberto(Table-" + value + ")");
        stack.push(new SymbolTable());
    }

    public SymbolTable getCurrentScope() {
        return stack.peek();
    }

    public void dropCurrentScope() {
        if(debugMode) 
            System.out.println(String.join("", Collections.nCopies(value, " ")) 
                + "Escopo Fechado(Table-" + value + ")");
        value--;
        stack.pop();
    }

    public List<SymbolTable> getAllSymbolTable() {
        return stack;
    }

    public int identationOffset(){
        return value + 1;
    }
}

