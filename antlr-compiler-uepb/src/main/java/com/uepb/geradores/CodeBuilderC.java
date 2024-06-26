package com.uepb.geradores;

import static com.uepb.semantic.SymbolTable.UEPBLanguageType.INVALIDO;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.STRING;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.uepb.UEPBLanguageBaseVisitor;
import com.uepb.UEPBLanguageParser.*;
import com.uepb.semantic.Scope;
import com.uepb.semantic.SymbolTable;
import com.uepb.semantic.Utils;
import com.uepb.semantic.SymbolTable.UEPBLanguageType;

public class CodeBuilderC extends UEPBLanguageBaseVisitor<Void> implements Gerador<Void>{
    
    private final StringBuilder output;
    private final Scope scopes;

    public CodeBuilderC(){
        output = new StringBuilder();
        scopes = new Scope(false);
        loadDefaultFunctions();
    }

    private void loadDefaultFunctions(){
        InputStream is = getClass().getClassLoader()
            .getResourceAsStream("defaultFunctions.c");
            
        if(is != null){
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                
                String row;
                while((row = reader.readLine()) != null){
                    output.append(String.format("%s\n", row));
                }

            }catch(IOException ex){
                throw new RuntimeException("(Gerador de Código) -> Erro ao carregar default functions");
            }
        }
    }

    public void tab(){
        output.append(String.join("", Collections.nCopies(scopes.identationOffset(), "    ")));
    }

    private boolean notNull(ParserRuleContext rule){
        return rule != null;
    }

    public String getGeneratedCode(){
        return output.toString();
    }

    @Override
    public Void visitPrograma(ProgramaContext ctx) {
        scopes.startScope();

        output.append("int main(){\n");
        
        visitListaComandos(ctx.listaComandos());   

        output.append("\n\n");
        tab();
        output.append("return 0;\n");

        closeScope(scopes.getCurrentScope());

        return null;
    }

    private void closeScope(SymbolTable table){

        table.getTable().stream()
            .filter(entry -> entry.getType() == STRING)
            .forEach(strVar -> {
                tab();
                output.append(String.format("free(%s);\n", 
                strVar.getName()));
            });

        scopes.dropCurrentScope();
        tab();
        output.append("}");
    }

    @Override
    public Void visitEscopo(EscopoContext ctx) {
        scopes.startScope();
        output.append("{\n");
        visitListaComandos(ctx.listaComandos());
        
        closeScope(scopes.getCurrentScope());

        return null;
    }

    @Override
    public Void visitListaComandos(ListaComandosContext ctx) {        
        ctx.comando().forEach(this::visitComando);
        return null;
    }

    @Override
    public Void visitComando(ComandoContext ctx) {
        
        tab();

        if(notNull(ctx.declaracao())){
            visitDeclaracao(ctx.declaracao());

        }else if(notNull(ctx.atribuicao())){
            visitAtribuicao(ctx.atribuicao());

        }else if(notNull(ctx.ifDecl())){
            visitIfDecl(ctx.ifDecl());

        }else if(notNull(ctx.whileDecl())){
            visitWhileDecl(ctx.whileDecl());

        }else if(notNull(ctx.print())){
            visitPrint(ctx.print());

        }else{
            visitAsk(ctx.ask());
        }

        output.append(";\n");

        return null;
    }

    @Override
    public Void visitDeclaracao(DeclaracaoContext ctx) {
        final var NOME_VAR = ctx.ID().getText();

        switch(ctx.TIPO_VAR().getText()){
            case "int":
                output.append(String.format("int %s", NOME_VAR));
                scopes.getCurrentScope().insert(NOME_VAR, UEPBLanguageType.INTEIRO);
                break;

            case "float":
                output.append(String.format("float %s", NOME_VAR));
                scopes.getCurrentScope().insert(NOME_VAR, UEPBLanguageType.FLOAT);
                break;

            case "string":
                output.append(String.format("char* %s", NOME_VAR));
                scopes.getCurrentScope().insert(NOME_VAR, UEPBLanguageType.STRING);
                break;

        }

        output.append(" = ");
        if(notNull(ctx.valor())){
            visitValor(ctx.valor());
            scopes.getCurrentScope().markInitialized(NOME_VAR);

        }else if(ctx.TIPO_VAR().getText().equals("int")){

            output.append("0");

        }else if(ctx.TIPO_VAR().getText().equals("float")){
            
            output.append("0.0");

        }else if(ctx.TIPO_VAR().getText().equals("string")){

            output.append("NULL");

        }

        return null;
    }

    @Override
    public Void visitAtribuicao(AtribuicaoContext ctx) {
        final var VAR_NAME = ctx.ID().getText();

        output.append(String.format("%s = ", VAR_NAME));
        visitValor(ctx.valor());

        return null;
    }

    @Override
    public Void visitValor(ValorContext ctx) {

        if(notNull(ctx.exprArit())){
            output.append(ctx.exprArit().getText());

        }else {
            visitCast(ctx.cast());

        }

        return null;
    }

    @Override
    public Void visitCast(CastContext ctx) {
        var TYPE = Utils.verifyType(scopes, ctx.valor());
        String appendValue = TYPE != UEPBLanguageType.STRING ? "\"" : "";

        switch(ctx.TIPO_VAR().getText()){
            case "int":
                output.append("atoi(").append(appendValue);
                visitValor(ctx.valor());
                output.append(appendValue).append(")");
                break;

            case "float":
                output.append("atof(").append(appendValue);
                visitValor(ctx.valor());
                output.append(appendValue).append(")");
                break;
        }
        return null;
    }

    @Override
    public Void visitIfDecl(IfDeclContext ctx) {
        
        output.append("if(");
        visitExprRel(ctx.exprRel());
        output.append(")");
        visitEscopo(ctx.escopoIf);
        if(ctx.escopoElse != null){
            output.append("else");
            visitEscopo(ctx.escopoElse);
        }

        return null;
    }

    @Override
    public Void visitWhileDecl(WhileDeclContext ctx) {
        
        output.append("while(");
        visitExprRel(ctx.exprRel());
        output.append(")");
        visitEscopoWhile(ctx.escopoWhile());

        return null;
    }

    @Override
    public Void visitEscopoWhile(EscopoWhileContext ctx) {
        
        scopes.startScope();
        output.append("{\n");
        
        if(!ctx.comando().isEmpty()){
            ctx.comando().forEach(this::visitComando);
        }

        closeScope(scopes.getCurrentScope());

        return null;
    }

    @Override
    public Void visitExprRel(ExprRelContext ctx) {
        
        visitTermoRel(ctx.termoRel(0));

        for(int i = 0; i < ctx.OP_BOOL().size(); i++){
            var op = ctx.OP_BOOL(0).getText();

            output.append(op.equals("and") ? " && " : " || ");
            visitTermoRel(ctx.outrosTermos.get(i));
        }
        
        return null;
    }

    @Override
    public Void visitTermoRel(TermoRelContext ctx) {
        
        if(notNull(ctx.v1) && notNull(ctx.v2)){
            visitValor(ctx.v1);
            output.append(" ").append(ctx.OP_REL().getText()).append(" ");
            visitValor(ctx.v2);
        }else{
            output.append("(");
            visitExprRel(ctx.exprRel());
            output.append(")");
        }

        return null;
    }

    @Override
    public Void visitAsk(AskContext ctx) {
        final var VAR_NAME = ctx.ID().getText();
        final var VAR_TYPE = scopes.getAllSymbolTable().stream()
                .filter(e -> e.exists(VAR_NAME))
                .map(e -> e.verify(VAR_NAME))
                .findFirst()
                .orElse(INVALIDO);

        if(VAR_TYPE == UEPBLanguageType.INTEIRO){
            output.append(VAR_NAME).append(" = atoi(ask())");
        }else {
            output.append(VAR_NAME).append(" = atof(ask())");
        }


        return null;
    }

    @Override
    public Void visitPrint(PrintContext ctx) {
        var TYPE = ctx.STRING() != null ? STRING : Utils.verifyType(scopes, ctx.valor());

        switch (TYPE) {
            case INTEIRO:
                output.append("printf(\"%d\", ");
                visitValor(ctx.valor());
                break;
            case FLOAT:
                output.append("printf(\"%f\", ");
                visitValor(ctx.valor());
                break;
            case STRING:
                output.append("printf(\"%s\", ").append(ctx.STRING().getText());
                break;
            default:
                break;
        }

        output.append(")");

        return null;
    }

}