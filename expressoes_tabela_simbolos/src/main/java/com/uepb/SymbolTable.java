package com.uepb;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    
    public record TableInput(String nome, double valor){}

    private final Map<String, TableInput> table;

    public SymbolTable(){
        table = new HashMap<>();
    }

    public void insert(String symbolName, double symbolValue){
        table.put(symbolName, new TableInput(symbolName, symbolValue));
    }

    public TableInput verify(String symbolName){
        return table.get(symbolName);
    }

}
