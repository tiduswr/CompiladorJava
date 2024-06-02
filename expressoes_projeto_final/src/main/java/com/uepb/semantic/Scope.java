package com.uepb.semantic;

import java.util.LinkedList;
import java.util.List;

public class Scope {

    private final LinkedList<SymbolTable> stack;

    public Scope() {
        stack = new LinkedList<>();
    }

    public void startScope() {
        stack.push(new SymbolTable());
    }

    public SymbolTable getCurrentScope() {
        return stack.peek();
    }

    public void dropCurrentScope() {
        stack.pop();
    }

    public List<SymbolTable> getAllSymbolTable() {
        return stack;
    }

    public Integer findEnderecoInGlobalScope(String id){
        for(var table : getAllSymbolTable()){
            var endereco = table.retrieveEndereco(id);
            if(endereco != null){
                return endereco;
            }
        }

        return null;
    }
}