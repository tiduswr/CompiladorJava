package com.uepb.semantic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    
    public enum UEPBLanguageType{
        INTEIRO,
        FLOAT,
        STRING,
        INVALIDO
    }

    //Initialized se refere ao valor da variavel, caso ela n tenha sido atribuído nenhum valor, sera false
    public class TableInput{
        private String name;
        private Boolean initialized;
        private UEPBLanguageType type;
        private int endereco;

        public TableInput(String name, UEPBLanguageType type, Boolean initialized){
            this.name = name;
            this.type = type;
            this.initialized = initialized;
        }

        // Especifico para o PCODE
        public TableInput(String name, int endereco){
            this.endereco = endereco;
            this.name = name;
        }

        // Especifico para o PCODE
        public void setEndereco(int endereco){
            this.endereco = endereco;
        }

        // Especifico para o PCODE
        public int getEndereco(){
            return endereco;
        }

        public boolean getInitialized(){
            return initialized;
        }

        public String getName(){
            return name;
        }

        public UEPBLanguageType getType(){
            return type;
        }

        public void setInitialized(boolean status){
            initialized = status;
        }

    }

    private final Map<String, TableInput> table;

    public SymbolTable(){
        table = new HashMap<>();
    }

    public void insert(String symbolName, UEPBLanguageType symbolType){
        table.put(symbolName, new TableInput(symbolName, symbolType, false));
    }


    // Especifico para o PCODE
    public void insert(String symbolName, int endereco){
        table.put(symbolName, new TableInput(symbolName, endereco));
    }

    public Collection<TableInput> getTable(){
        return table.values();
    }

    public boolean isInitialized(String varName){
        var record = table.get(varName);
        return record != null ? record.getInitialized() : false;
    }

    public boolean markInitialized(String varName){
        var record = table.get(varName);
        if(record != null){
            record.setInitialized(true);
            return true;
        }

        return false;
    }

    public UEPBLanguageType verify(String symbolName){
        var record = table.get(symbolName);
        return record != null ? record.getType() : null;
    }

    // Especifico para o PCODE
    public Integer retrieveEndereco(String symbolName){
        var record = table.get(symbolName);
        return record != null ? record.getEndereco() : null;
    }

    public int[] retrieveEnderecos(){
        int[] values = new int[table.values().size()];
        int index = 0;

        for(var value : table.values()){
            values[index++] = value.getEndereco();
        }

        return values;
    }

    public boolean exists(String symbolName){
        return table.containsKey(symbolName);
    }

}
