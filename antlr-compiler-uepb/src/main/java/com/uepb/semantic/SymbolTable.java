package com.uepb.semantic;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    
    public enum UEPBLanguageType{
        INTEIRO,
        FLOAT,
        BOOLEAN,
        STRING,
        INVALIDO
    }

    public record TableInput(String name, UEPBLanguageType type){}

    public final Map<String, TableInput> table;

    public SymbolTable(){
        table = new HashMap<>();
    }

    public void insert(String symbolName, UEPBLanguageType symbolType){
        table.put(symbolName, new TableInput(symbolName, symbolType));
    }

    public UEPBLanguageType verify(String symbolName){
        var record = table.get(symbolName);
        return record != null ? record.type() : null;
    }

    public boolean exists(String symbolName){
        return table.containsKey(symbolName);
    }

}
