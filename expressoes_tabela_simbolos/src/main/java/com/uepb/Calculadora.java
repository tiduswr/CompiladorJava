package com.uepb;

import com.uepb.ExpressoesParser.DeclaracaoContext;
import com.uepb.ExpressoesParser.ExprContext;
import com.uepb.ExpressoesParser.FatorContext;
import com.uepb.ExpressoesParser.ProgramaContext;
import com.uepb.ExpressoesParser.TermoContext;

public class Calculadora extends ExpressoesBaseVisitor<Double> {
    
    private final Scope escopos;

    public Calculadora(){
        escopos = new Scope();
    }

    private Double toDouble(String expr){
        return Double.parseDouble(expr);
    }

    @Override
    public Double visitPrograma(ProgramaContext ctx) {
        return visitExpr(ctx.expr());
    }

    @Override
    public Double visitExpr(ExprContext ctx) {
        var valor = visitTermo(ctx.TERMO);

        for(int i = 0; i < ctx.TERMOS.size(); i++){
            final var TERMO = ctx.TERMOS.get(i);
            final var OP = ctx.OP1(i).getText();

            if(OP.equals("+")){
                valor += visitTermo(TERMO);
            }else{
                valor -= visitTermo(TERMO);
            }
        }
        
        return valor;
    }

    @Override
    public Double visitTermo(TermoContext ctx) {
        var valor = visitFator(ctx.FATOR);

        for(int i = 0; i < ctx.FATORES.size(); i++){
            final var OP = ctx.OP2(i).getText();
            final var FATOR = ctx.FATORES.get(i);

            if(OP.equals("*")){
                valor *= visitFator(FATOR);
            }else{
                valor /= visitFator(FATOR);
            }
        }

        return valor;
    }

    @Override
    public Double visitFator(FatorContext ctx) {
        if(ctx.EXPR_PARENTESES != null){
            return visitExpr(ctx.EXPR_PARENTESES);
        }else if(ctx.CONST != null){
            return toDouble(ctx.CONST.getText());
        }else if(ctx.VAR != null){
            for(final var table : escopos.getAllSymbolTable()){
                var entrada = table.verify(ctx.VAR.getText());
                if(entrada != null){
                    return entrada.valor();
                }
            }
            throw new SemanticException("Erro semântico: A variável " + ctx.VAR.getText() + " não foi declarada");
        }else{
            escopos.startScope();
            visitListaDeclaracoes(ctx.listaDeclaracoes());
            double valor = visitExpr(ctx.SUB_EXPR);
            escopos.dropCurrentScope();

            return valor;
        }
    }

    @Override
    public Double visitDeclaracao(DeclaracaoContext ctx) {
        var escopoAtual = escopos.getCurrentScope();

        if(escopoAtual.verify(ctx.NOME.getText()) != null){
            throw new SemanticException("Erro semântico: A variável " + ctx.NOME.getText() + " ja foi declarada nesse escopo");
        }else{
            escopoAtual.insert(ctx.NOME.getText(), visitExpr(ctx.expr()));
        }

        return null;
    }

}
