package com.uepb.geradores;

import com.uepb.UEPBLanguageParser.ProgramaContext;

public interface Gerador<T> {
    
    public T visitPrograma(ProgramaContext ctx);
    public String getGeneratedCode();

}
