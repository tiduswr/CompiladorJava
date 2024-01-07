package com.uepb.parser.exceptions;

import java.util.Arrays;

import com.uepb.token.Token;
import com.uepb.token.TokenType;

public class SyntaxError extends RuntimeException{
    
    public SyntaxError(Token recebido, TokenType ...esperado){
        super("Erro sintático: Foi recebido " + recebido.lexema() 
            + " mas era esperado [" + String.join(", ", 
                Arrays.stream(esperado).map(TokenType::toString).toList()
            ) + "]");
    }

}
