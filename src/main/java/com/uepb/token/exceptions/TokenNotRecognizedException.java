package com.uepb.token.exceptions;

import java.io.IOException;

public class TokenNotRecognizedException extends IOException{
    
    public TokenNotRecognizedException(String message){
        super(message);
    }

}
