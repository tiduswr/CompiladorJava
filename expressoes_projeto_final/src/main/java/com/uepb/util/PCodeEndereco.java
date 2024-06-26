package com.uepb.util;

import java.util.Arrays;
import java.util.stream.IntStream;

public class PCodeEndereco {
    
    private boolean[] enderecos;

    public PCodeEndereco(){
        this.enderecos = new boolean[50];
    }

    public int getNextFreeEndereco(){
        int index = IntStream
            .range(0, enderecos.length)
            .filter(i -> !enderecos[i])
            .findFirst()
            .orElseThrow(
                () -> new RuntimeException("[PCODE] - Pilha de endereços cheia")
            );

        enderecos[index] = true;
        return index;
    }

    public void freeEndereco(int ...index){
        Arrays.stream(index)
            .filter(i -> i >= 0 && i < index.length)
            .forEach(i -> enderecos[i] = false);
    }

}