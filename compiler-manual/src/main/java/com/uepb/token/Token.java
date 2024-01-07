package com.uepb.token;

public record Token(
    TokenType type,
    String lexema
){
    @Override
    public String toString(){
        return "<" + type + ", " + lexema + ">";
    }
}
