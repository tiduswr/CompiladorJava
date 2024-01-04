package com.uepb.lexer;

import java.util.Map;

public class Utils {
    
    private static final Map<String, TokenType> reservedWords = 
        Map.of(
            "var", TokenType.PC_VAR,
            "int", TokenType.PC_INT,
            "float", TokenType.PC_FLOAT,
            "if", TokenType.PC_IF,
            "while", TokenType.PC_WHILE,
            "print", TokenType.PC_PRINT
        );
    
    private static final Map<Character, TokenType> reservedChars = 
        Map.of(
            '+', TokenType.OP_SUM,
            '-', TokenType.OP_SUBTRACT,
            '*', TokenType.OP_MULTIPLY,
            '/', TokenType.OP_DIVISION,
            ';', TokenType.SEMICOLON,
            ':', TokenType.COLON,
            '{', TokenType.OPEN_BRACE,
            '}', TokenType.CLOSE_BRACE,
            '(', TokenType.OPEN_PAREN,
            ')', TokenType.CLOSE_PAREN
        );

    public static boolean isReservedWord(String lexema){
        return reservedWords.containsKey(lexema);
    }

    public static TokenType reservedWordToTokenType(String lexema){
        return reservedWords.get(lexema);
    }

    public static boolean isReservedChar(char op){
        return reservedChars.containsKey(op);
    }

    public static TokenType reservedCharToTokenType(char op){
        return reservedChars.get(op);
    }

}
