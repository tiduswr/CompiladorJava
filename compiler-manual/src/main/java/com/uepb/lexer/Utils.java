package com.uepb.lexer;

import java.util.Map;
import static java.util.Map.entry;

import com.uepb.token.Token;
import com.uepb.token.TokenType;

public class Utils {
    
    private static final Map<String, TokenType> reservedWords = 
        Map.ofEntries(
            entry("spawn", TokenType.PC_VAR),
            entry("int", TokenType.PC_INT),
            entry("float", TokenType.PC_FLOAT),
            entry("string", TokenType.PC_STRING),
            entry("unless", TokenType.PC_IF),
            entry("during", TokenType.PC_WHILE),
            entry("show", TokenType.PC_PRINT),
            entry("stop", TokenType.PC_BREAK),
            entry("do", TokenType.PC_ELSE),
            entry("or", TokenType.OP_OR),
            entry("and", TokenType.OP_AND)
        );
    
    private static final Map<Character, TokenType> reservedChars = Map.ofEntries(
        entry('+', TokenType.OP_SUM),
        entry('-', TokenType.OP_SUBTRACT),
        entry('*', TokenType.OP_MULTIPLY),
        entry('/', TokenType.OP_DIVISION),
        entry('>', TokenType.OP_GREATER_THAN),
        entry('<', TokenType.OP_LOWER_THAN),
        entry('=', TokenType.OP_EQUALS),
        entry('!', TokenType.OP_DENIAL),
        entry(';', TokenType.SEMICOLON),
        entry(':', TokenType.COLON),
        entry('{', TokenType.OPEN_BRACE),
        entry('}', TokenType.CLOSE_BRACE),
        entry('(', TokenType.OPEN_PAREN),
        entry(')', TokenType.CLOSE_PAREN),
        entry(',', TokenType.COMMA)
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
