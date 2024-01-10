package com.tiduswr;

import com.tiduswr.ExpressoesParser.ExpressaoContext;
import com.tiduswr.ExpressoesParser.FatorContext;
import com.tiduswr.ExpressoesParser.ProgramaContext;
import com.tiduswr.ExpressoesParser.TermoContext;

public class Calculador extends ExpressoesBaseVisitor<Double>{
    
    @Override
    public Double visitPrograma(ProgramaContext ctx) {
        return visitExpressao(ctx.expressao());
    }

    @Override
    public Double visitExpressao(ExpressaoContext ctx) {
        var valor = visitTermo(ctx.t1);
        
        for(int i = 0; i < ctx.termos.size(); i++){
            var op1 = ctx.op1(i);
            var termo2 = ctx.termos.get(i);

            if(op1.getText().equals("+")){
                valor += visitTermo(termo2);
            }else{
                valor -= visitTermo(termo2);
            }
        }

        return valor;
    }

    @Override
    public Double visitTermo(TermoContext ctx) {
        var valor = visitFator(ctx.f1);

        for(int i = 0; i < ctx.fatores.size(); i++){
            var op2 = ctx.op2(i);
            var fator2 = ctx.fatores.get(i);

            if(op2.getText().equals("*")){
                valor *= visitFator(fator2);
            }else{
                valor /= visitFator(fator2);
            }
        }

        return valor;
    }

    @Override
    public Double visitFator(FatorContext ctx) {
        if(ctx.NUM() != null){
            return Double.parseDouble(ctx.NUM().getText());
        }else{
            return visitExpressao(ctx.expressao());
        }
    }

}
