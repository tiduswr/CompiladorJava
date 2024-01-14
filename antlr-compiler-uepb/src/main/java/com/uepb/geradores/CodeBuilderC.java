package com.uepb.geradores;

import com.uepb.UEPBLanguageBaseVisitor;
import com.uepb.UEPBLanguageParser.ComandoContext;
import com.uepb.UEPBLanguageParser.EscopoContext;
import com.uepb.UEPBLanguageParser.ListaComandosContext;
import com.uepb.UEPBLanguageParser.ProgramaContext;
import com.uepb.semantic.Scope;

public class CodeBuilderC extends UEPBLanguageBaseVisitor<Void>{
    
    private final StringBuilder output;
    private final Scope scopes;

    public CodeBuilderC(){
        output = new StringBuilder();
        scopes = new Scope(false);
    }

    public String getGeneratedCode(){
        return output.toString();
    }

    @Override
    public Void visitPrograma(ProgramaContext ctx) {
        scopes.startScope();

        output.append("#include <stdio.h>\n#include <stdlib.h>\n\n");
        output.append("int main(){\n");
        
        visitListaComandos(ctx.listaComandos());   

        output.append("\n\nreturn 0;\n}");

        scopes.dropCurrentScope();

        return null;
    }

    @Override
    public Void visitEscopo(EscopoContext ctx) {
        scopes.startScope();
        visitListaComandos(ctx.listaComandos());
        scopes.dropCurrentScope();

        return null;
    }

    @Override
    public Void visitListaComandos(ListaComandosContext ctx) {        
        ctx.comando().forEach(comando -> visitComando(comando));
        return null;
    }

    @Override
    public Void visitComando(ComandoContext ctx) {
        
        return null;
    }

}
