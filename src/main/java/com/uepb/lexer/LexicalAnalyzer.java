package com.uepb.lexer;

import java.io.IOException;

import com.uepb.token.Token;
import com.uepb.token.TokenType;
import com.uepb.token.exceptions.TokenNotRecognizedException;

public class LexicalAnalyzer {
    
    private final DoubleBufferReader reader;
    private final boolean ignoreWhiteSpace;

    public LexicalAnalyzer(String programSource, boolean ignoreWhiteSpace) throws IOException{
        this.ignoreWhiteSpace = ignoreWhiteSpace;
        reader = new DoubleBufferReader(programSource);
    }

    public Token readNextToken() throws IOException, TokenNotRecognizedException{
        int readedChar;

        while((readedChar = reader.readNextChar()) != DoubleBufferReader.FILE_END){
            char c = (char) readedChar;

            // Ignora espaços em branco e quebras de linha
            if(Character.isWhitespace(c)){
                if(ignoreWhiteSpace) continue;
                if(c == '\n'){
                    return new Token(TokenType.LINE_BREAK, "\n");
                }else if(c == ' '){
                    return new Token(TokenType.WHITE_SPACE, " ");
                }else{
                    continue;
                }
            }

            // Identifica palavras-chave e identificadores
            if(Character.isLetter(c)) return readIdentifier(c);
            
            // Identifica números
            if(Character.isDigit(c)) return readNumber(c);

            //Identifica caracteres especiais
            if(Utils.isReservedChar(c)) return readSpecialCharacters(c);

            throw new TokenNotRecognizedException("O caractere '" + c + "' não foi reconhecido no buffer");
        }

        return null;
    }

    private Token readSpecialCharacters(char c) throws IOException, TokenNotRecognizedException{

        switch(c){
            case '>':
                return binaryCheck(reader, c, '=', 
                    TokenType.OP_GREATER_OR_EQUAL, 
                    TokenType.OP_GREATER_THAN);                
            case '<':
                return binaryCheck(reader, c, '=', 
                    TokenType.OP_LOWER_OR_EQUAL, 
                    TokenType.OP_LOWER_THAN);
            case '=':
                return binaryCheck(reader, c, '=', 
                    TokenType.OP_EQUALS, 
                    TokenType.OP_ASSIGNMENT);
            case '!':
                return binaryCheck(reader, c, '=', 
                    TokenType.OP_NOT_EQUAL, 
                    TokenType.OP_DENIAL);
            default:
                return new Token(
                    Utils.reservedCharToTokenType(c),
                    "" + c
                );
        }

    }

    private Token binaryCheck(DoubleBufferReader reader, char curChar, 
        char possibleNextChar, TokenType ifTrue, TokenType ifNot) throws IOException{

        StringBuilder operator = new StringBuilder("" + curChar);

        if(reader.readNextChar() == possibleNextChar){

            operator.append(possibleNextChar);
            return new Token(ifTrue, operator.toString());

        }else{

            reader.undo();
            return new Token(ifNot, operator.toString());

        }

    }

    private Token readIdentifier(char initialChar) throws IOException{
        StringBuilder identifier = new StringBuilder();
        identifier.append(initialChar);

        int readedChar;
        while((readedChar = reader.readNextChar()) != DoubleBufferReader.FILE_END){
            char c = (char) readedChar;
            if(Character.isLetterOrDigit(c)){
                identifier.append(c);
            }else{
                reader.undo();
                break;
            }
        }

        String lexema = identifier.toString();
        TokenType type = TokenType.IDENTIFIER;
        if(Utils.isReservedWord(lexema)){
            type = Utils.reservedWordToTokenType(lexema);
        }

        return new Token(type, lexema);
    }

    private Token readNumber(char initialChar) throws IOException{
        StringBuilder number = new StringBuilder();
        number.append(initialChar);

        int readedChar;
        boolean hasDecimalPoint = false;
        while((readedChar = reader.readNextChar()) != DoubleBufferReader.FILE_END){
            char c = (char) readedChar;
            if(Character.isDigit(c)){
                number.append(c);
            }else if(c == '.' && !hasDecimalPoint){
                number.append(c);
                hasDecimalPoint = true;
            }else{
                reader.undo();
                break;
            }
        }

        return new Token(
            hasDecimalPoint ? TokenType.PC_FLOAT : TokenType.PC_INT, 
            number.toString()
        );
    }

}
