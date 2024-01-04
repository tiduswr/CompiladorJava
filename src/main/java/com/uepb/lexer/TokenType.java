package com.uepb.lexer;

public enum TokenType {
    // Palavras-chave
    PC_VAR,
    PC_INT,
    PC_FLOAT,
    PC_IF,
    PC_WHILE,
    PC_PRINT,
    PC_BREAK,

    // Operadores
    OP_ASSIGNMENT, // =
    OP_EQUALS,     // ==
    OP_DENIAL,  // !
    OP_NOT_EQUAL,  // !=
    OP_GREATER_THAN, // >
    OP_LOWER_THAN, // <
    OP_GREATER_OR_EQUAL, // >=
    OP_LOWER_OR_EQUAL, // <=
    OP_SUM,       // +
    OP_SUBTRACT,      // -
    OP_MULTIPLY,   // *
    OP_DIVISION,  // /
    
    // Identificadores
    IDENTIFIER,

    // Ponto e vírgula e chaves
    WHITE_SPACE,
    LINE_BREAK,
    COLON,
    SEMICOLON, // ;
    OPEN_BRACE,  // {
    CLOSE_BRACE, // }
    OPEN_PAREN, // (
    CLOSE_PAREN, // )
}

