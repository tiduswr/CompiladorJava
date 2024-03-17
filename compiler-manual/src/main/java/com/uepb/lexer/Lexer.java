package com.uepb.lexer;

import java.io.IOException;

import com.uepb.token.Token;
import com.uepb.token.TokenType;
import com.uepb.token.exceptions.TokenNotRecognizedException;

public class Lexer {
    
    private final CharBuffer reader;
    private final boolean ignoreWhiteSpace;

    public Lexer(String programSource, boolean ignoreWhiteSpace) throws IOException{
        this.ignoreWhiteSpace = ignoreWhiteSpace;
        reader = new CharBuffer(programSource);
    }

    public Token readNextToken() throws IOException {
        int readedChar;

        while((readedChar = reader.readNextChar()) != CharBuffer.FILE_END){
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
            } else if (c == '"') {
                return readString(c);
            } else if (Character.isLetter(c)) {
                return readIdentifier(c);
            } else if (Character.isDigit(c)) {
                return readNumber(c);
            } else if (Utils.isReservedChar(c)) {
                return readSpecialCharacters(c);
            } else {
                throw new TokenNotRecognizedException("O caractere '" + c + "' não foi reconhecido no buffer");
            }
        }

        return new Token(TokenType.EOF, null);
    }

    private Token readSpecialCharacters(char c) throws IOException {

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

    private Token binaryCheck(CharBuffer reader, char curChar, 
        char possibleNextChar, TokenType ifTrue, TokenType ifNot) throws IOException {

        StringBuilder operator = new StringBuilder("" + curChar);

        char c = (char) reader.readNextChar();
        if(c == possibleNextChar){

            operator.append(possibleNextChar);
            return new Token(ifTrue, operator.toString());

        }else{

            reader.pushback(c);
            return new Token(ifNot, operator.toString());

        }

    }

    private Token readString(char initialChar) throws IOException {
        StringBuilder str = new StringBuilder();
        str.append(initialChar);
    
        int readedChar;
        while ((readedChar = reader.readNextChar()) != CharBuffer.FILE_END) {
            char c = (char) readedChar;
    
            if (c == '"') {
                str.append(c);
                return new Token(TokenType.STRING, str.toString());
            } else if (c == '\\') {
                int nextChar = reader.readNextChar();
                if (nextChar == 'n') {
                    str.append('\n');
                } else if (nextChar == 't') {
                    str.append('\t');
                } else if (nextChar == '\\') {
                    str.append('\\');
                } else if (nextChar == '"') {
                    str.append('"');
                } else {
                    throw new TokenNotRecognizedException("Sequência de escape inválida: \\" + (char) nextChar);
                }
            } else {
                str.append(c);
            }
        }

        throw new TokenNotRecognizedException("Fim de arquivo inesperado dentro de uma string.");
    }
    

    private Token readIdentifier(char initialChar) throws IOException {
        StringBuilder identifier = new StringBuilder();
        identifier.append(initialChar);

        int readedChar;
        while((readedChar = reader.readNextChar()) != CharBuffer.FILE_END){
            char c = (char) readedChar;
            if(Character.isLetterOrDigit(c)){
                identifier.append(c);
            }else{
                reader.pushback(c);
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

    private Token readNumber(char initialChar) throws IOException {
        StringBuilder number = new StringBuilder();
        number.append(initialChar);

        int readedChar;
        boolean hasDecimalPoint = false;
        while((readedChar = reader.readNextChar()) != CharBuffer.FILE_END){
            char c = (char) readedChar;
            if(Character.isDigit(c)){
                number.append(c);
            }else if(!hasDecimalPoint && c == '.'){
                number.append(c);
                hasDecimalPoint = true;
            }else{
                reader.pushback(c);
                break;
            }
        }

        return new Token(
            hasDecimalPoint ? TokenType.FLOAT : TokenType.INT, 
            number.toString()
        );
    }

}
