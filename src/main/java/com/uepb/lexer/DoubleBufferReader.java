package com.uepb.lexer;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.uepb.lexer.exceptions.ProgramReadException;

public class DoubleBufferReader implements Closeable{
    
    private final static int BUFFER_SIZE = 20;
    private final static int BUFFER_COPIES = 2;
    private final static int BUFFER_TOTAL_SIZE = BUFFER_COPIES * BUFFER_SIZE;
    public final static int FILE_END = -1;

    private int[] buffer;
    private int pointer, currentBuffer;

    private InputStream is;

    public DoubleBufferReader(String path) throws ProgramReadException{

        try{
            is = new FileInputStream(new File(path));
            bufferInit();
        }catch(IOException ex){
            String errorMessage = "Erro na leitura do arquivo fonte do programa";

            try{
                this.close();
            }catch(IOException exception){
                errorMessage += ";\n" + exception.getMessage();
            }

            throw new ProgramReadException(errorMessage);
        }   

    }

    private void bufferInit() throws IOException{
        currentBuffer = 1; // Para que o loadBuffer seja iniciado pela primeira vez

        buffer = new int[BUFFER_TOTAL_SIZE];
        pointer = 0;
        loadBuffer(0);
    }

    private void advancePointer() throws IOException{
        pointer++;
        if(pointer == BUFFER_SIZE){
            loadBuffer(1);
        }else if(pointer == BUFFER_TOTAL_SIZE){
            loadBuffer(0);
            pointer = 0;
        }
    }

    private void loadBuffer(int currentBuffer) throws IOException{
        if(currentBuffer != this.currentBuffer){
            this.currentBuffer = currentBuffer;

            int initialValue = currentBuffer * (BUFFER_SIZE);
            int finalValue = BUFFER_SIZE * (currentBuffer + 1);

            for(int i = initialValue; i < finalValue; i++){
                buffer[i] = is.read();
                if(buffer[i] == FILE_END) break;
            }
        }
    }

    public int readNextChar() throws IOException {
        int currentChar = buffer[pointer];
        advancePointer();
        return currentChar;
    }

    public void undo(){
        pointer--;
        if(pointer < 0) pointer = BUFFER_TOTAL_SIZE - 1;
    }

    public char getCurrentChar(){
        return (char) buffer[pointer];
    }

    @Override
    public void close() throws IOException {
        if(is != null) is.close();
    }

}
