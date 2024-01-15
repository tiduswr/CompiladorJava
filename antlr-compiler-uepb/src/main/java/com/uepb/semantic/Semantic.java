package com.uepb.semantic;

import static com.uepb.semantic.SymbolTable.UEPBLanguageType.FLOAT;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.INTEIRO;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.INVALIDO;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.STRING;

import java.util.Collections;

import com.uepb.UEPBLanguageBaseVisitor;
import com.uepb.UEPBLanguageParser.AtribuicaoContext;
import com.uepb.UEPBLanguageParser.DeclaracaoContext;
import com.uepb.UEPBLanguageParser.EscopoContext;
import com.uepb.UEPBLanguageParser.EscopoWhileContext;
import com.uepb.UEPBLanguageParser.ExprAritContext;
import com.uepb.UEPBLanguageParser.ExprRelContext;
import com.uepb.UEPBLanguageParser.ProgramaContext;
import com.uepb.UEPBLanguageParser.TermoRelContext;
import com.uepb.semantic.SymbolTable.UEPBLanguageType;

public class Semantic extends UEPBLanguageBaseVisitor<Void>{
    private final Scope scopes;
    private final boolean debugMode;

    public Semantic(boolean debugMode){
        super();
        this.debugMode = debugMode;
        scopes = new Scope(debugMode);
    }

    @Override
    public Void visitPrograma(ProgramaContext ctx) {
        scopes.startScope();
        super.visitPrograma(ctx);
        scopes.dropCurrentScope();

        return null;
    }

    @Override
    public Void visitEscopo(EscopoContext ctx) {
        scopes.startScope();
        super.visitListaComandos(ctx.listaComandos());
        scopes.dropCurrentScope();

        return null;
    }

    @Override
    public Void visitEscopoWhile(EscopoWhileContext ctx) {
        scopes.startScope();
        ctx.comando().forEach(c -> visitComando(c));
        scopes.dropCurrentScope();

        return null;
    }

    @Override
    public Void visitDeclaracao(DeclaracaoContext ctx) {
        UEPBLanguageType varTypeToEnum = INVALIDO;
        UEPBLanguageType atribType = INVALIDO;
        final String varName = ctx.ID().getText();
        final String varType = ctx.TIPO_VAR().getText();        
        
        switch (varType) {
            case "int": varTypeToEnum = INTEIRO; break;
            case "float": varTypeToEnum = FLOAT; break;
            case "string": varTypeToEnum = STRING; break;
            default: break;
        }

        if(ctx.valor() != null) atribType = Utils.verifyType(scopes, ctx.valor());    

        final var currentScope = scopes.getCurrentScope();
        
        if(debugMode) 
            System.out.println(String.join("", Collections.nCopies(scopes.identationOffset(), " ")) 
                + "(Declaracao) -> " + ctx.getText());

        if(currentScope.exists(varName)){
            Utils.insertSemanticError(ctx.ID().getSymbol(), "Variável " + varName + " já foi declarada");
        }else if(atribType != INVALIDO && atribType != varTypeToEnum){
            Utils.insertSemanticError(ctx.ID().getSymbol(), "Variável " + varName + " não aceita a expressão '" 
                + ctx.valor().getText() +"' como atribuição, pois o tipo é incompatível");
        }else{
            currentScope.insert(varName, varTypeToEnum);
        }

        return null;
    }

    @Override
    public Void visitAtribuicao(AtribuicaoContext ctx) {
        UEPBLanguageType exprType = Utils.verifyType(scopes, ctx.valor());

        if(exprType != INVALIDO){
            var allScopes = scopes.getAllSymbolTable();
            String varName = ctx.ID().getText();

            if(!allScopes.stream().anyMatch(t -> t.exists(varName))){
                Utils.insertSemanticError(ctx.ID().getSymbol(), "Variável " + varName + " não foi declarada");
            }else{
                var varType = Utils.verifyType(scopes.getAllSymbolTable(), varName);

                if(varType != exprType){
                    Utils.insertSemanticError(ctx.ID().getSymbol(), "A variável " + varName + " não é compatível com a expressão");
                }
            }
        }

        return null;
    }

    @Override
    public Void visitExprArit(ExprAritContext ctx) {
        Utils.verifyType(scopes, ctx);
        return null;
    }

    @Override
    public Void visitExprRel(ExprRelContext ctx) {
        ctx.termoRel().forEach(t -> Utils.verifyType(scopes, t));

        return null;
    }

    @Override
    public Void visitTermoRel(TermoRelContext ctx) {
        
        //Verificar declaração de variáveis
        for(final var VALOR : ctx.valor()){
            Utils.verifyType(scopes, VALOR);
        }

        return null;
    }

}
