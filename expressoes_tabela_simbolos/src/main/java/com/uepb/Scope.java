package com.uepb;

import java.util.LinkedList;
import java.util.List;

public class Scope {
    private LinkedList<SymbolTable> stack;

    public Scope(){
        stack = new LinkedList<>();
        startScope();
    }

    public void startScope() {
        stack.push(new SymbolTable());
    }

    public SymbolTable getCurrentScope(){
        return stack.peek();
    }

    public void dropCurrentScope(){
        stack.pop();
    }

    public List<SymbolTable> getAllSymbolTable(){
        return stack;
    }

}
