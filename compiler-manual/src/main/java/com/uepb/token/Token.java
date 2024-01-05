package com.uepb.token;

public class Token {
    public TokenType type;
    public String lexema;

    public Token(TokenType type, String lexema){
        this.lexema = lexema;
        this.type = type;
    }

    @Override
    public String toString(){
        return "<" + type + ", " + lexema + ">";
    }
}
