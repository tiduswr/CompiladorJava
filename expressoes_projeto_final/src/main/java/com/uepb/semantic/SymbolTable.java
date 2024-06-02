package com.uepb.semantic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    //Initialized se refere ao valor da variavel, caso ela n tenha sido atribuído nenhum valor, sera false
    public class TableInput{
        private String name;
        private Double valor;
        private Integer endereco;

        public TableInput(String name, double valor){
            this.name = name;
            this.valor = valor;
        }

        // Especifico para o PCODE
        public TableInput(String name, int endereco){
            this.endereco = endereco;
            this.name = name;
        }

        // Especifico para o PCODE
        public Integer getEndereco(){
            return endereco;
        }

        public String getName(){
            return name;
        }

        public Double getValor() {
            return valor;
        }
    }

    private final Map<String, TableInput> table;

    public SymbolTable(){
        table = new HashMap<>();
    }

    public void insert(String symbolName, double valor){
        table.put(symbolName, new TableInput(symbolName, valor));
    }

    // Especifico para o PCODE
    public void insertWithEndereco(String symbolName, int endereco){
        table.put(symbolName, new TableInput(symbolName, endereco));
    }

    public Collection<TableInput> getTable(){
        return table.values();
    }

    public TableInput verify(String symbolName){
        return table.get(symbolName);
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