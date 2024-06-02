package com.uepb;

import org.antlr.v4.runtime.Token;

import com.uepb.ExpressoesParser.DeclaracaoContext;
import com.uepb.ExpressoesParser.ExprContext;
import com.uepb.ExpressoesParser.Expr_relContext;
import com.uepb.ExpressoesParser.FatorContext;
import com.uepb.ExpressoesParser.Fator_relContext;
import com.uepb.ExpressoesParser.ProgramaContext;
import com.uepb.ExpressoesParser.TermoContext;
import com.uepb.ExpressoesParser.Termo_relContext;
import com.uepb.semantic.Scope;
import com.uepb.semantic.SemanticException;

public class Calculadora extends ExpressoesBaseVisitor<Double> {
    
    private final Scope escopos;
    private final double TRUE = 1.0;
    private final double FALSE = 0.0;

    public Calculadora() {
        escopos = new Scope();
    }

    private Double toDouble(String expr) {
        return Double.parseDouble(expr);
    }

    @Override
    public Double visitPrograma(ProgramaContext ctx) {
        return visitExpr(ctx.expr());
    }

    @Override
    public Double visitExpr(ExprContext ctx) {
        var valor = visitTermo(ctx.TERMO);

        for(int i = 0; i < ctx.TERMOS.size(); i++) {
            final var TERMO = ctx.TERMOS.get(i);
            final var OP = ctx.OP1(i).getText();

            if (OP.equals("+")) {
                valor += visitTermo(TERMO);
            } else {
                valor -= visitTermo(TERMO);
            }
        }

        return valor;
    }

    @Override
    public Double visitTermo(TermoContext ctx) {
        var valor = visitFator(ctx.FATOR);

        for(int i = 0; i < ctx.FATORES.size(); i++) {
            final var OP = ctx.OP2(i).getText();
            final var FATOR = ctx.FATORES.get(i);

            if (OP.equals("*")) {
                valor *= visitFator(FATOR);
            } else {
                valor /= visitFator(FATOR);
            }
        }

        return valor;
    }

    @Override
    public Double visitFator(FatorContext ctx) {
        if (ctx.EXPR_PARENTESES != null) {
            return visitExpr(ctx.EXPR_PARENTESES);
        } else if (ctx.CONST != null) {
            return toDouble(ctx.CONST.getText());
        } else if (ctx.VAR != null) {
            for (final var table : escopos.getAllSymbolTable()) {
                var entrada = table.verify(ctx.VAR.getText());
                if (entrada != null) {
                    return entrada.getValor();
                }
            }
            throw new SemanticException("Erro semântico: A variável " + ctx.VAR.getText() + " não foi declarada");
        } else if (ctx.expr_rel() != null) {
            double valorRel = visitExpr_rel(ctx.expr_rel());

            if (valorRel == TRUE) {
                return visitExpr(ctx.IF_TRUE);
            } else if (ctx.IF_FALSE != null) {
                return visitExpr(ctx.IF_FALSE);
            } else {
                return 0.0; //Default
            }

        } else {
            escopos.startScope();
            visitListaDeclaracoes(ctx.listaDeclaracoes());
            double valor = visitExpr(ctx.SUB_EXPR);
            escopos.dropCurrentScope();
            return valor;
        }
    }

    @Override
    public Double visitExpr_rel(Expr_relContext ctx) {
        var valor = visitTermo_rel(ctx.TERMO);

        for (int i = 0; i < ctx.TERMOS.size(); i++) {
            final var TERMO = ctx.TERMOS.get(i);
            final var OP = ctx.OP_BOOL(i).getText();

            if (OP.equals("and")) {
                valor = (valor == TRUE && visitTermo_rel(TERMO) == TRUE) ? TRUE : FALSE;
            } else {
                valor = (valor == TRUE || visitTermo_rel(TERMO) == TRUE) ? TRUE : FALSE;
            }
        }

        return valor;
    }

    @Override
    public Double visitTermo_rel(Termo_relContext ctx) {
        if (ctx.fator_rel(0) != null) {
            double v1 = visitFator_rel(ctx.fator_rel(0));
            double v2 = visitFator_rel(ctx.fator_rel(1));
            String op = ctx.OP_REL().getText();

            switch (op) {
                case ">":
                    return v1 > v2 ? TRUE : FALSE;
                case ">=":
                    return v1 >= v2 ? TRUE : FALSE;
                case "<":
                    return v1 < v2 ? TRUE : FALSE;
                case "<=":
                    return v1 <= v2 ? TRUE : FALSE;
                case "==":
                    return v1 == v2 ? TRUE : FALSE;
                case "!=":
                    return v1 != v2 ? TRUE : FALSE;
                default:
                    throw createSemanticException(ctx.OP_REL().getSymbol(), "Operador relacional desconhecido: " + ctx.OP_REL().getText());
            }
        } else {
            return visitExpr_rel(ctx.expr_rel());
        }
    }

    @Override
    public Double visitFator_rel(Fator_relContext ctx) {
        if (ctx.VAR != null) {
            for (final var table : escopos.getAllSymbolTable()) {
                var entrada = table.verify(ctx.VAR.getText());
                if (entrada != null) {
                    return entrada.getValor();
                }
            }
            throw createSemanticException(ctx.VAR, "A variável " + ctx.VAR.getText() + " não foi declarada");
        } else if(ctx.EXPR != null){
            return visitExpr(ctx.EXPR);    
        }else {
            return toDouble(ctx.CONST.getText());
        }
    }

    @Override
    public Double visitDeclaracao(DeclaracaoContext ctx) {
        var escopoAtual = escopos.getCurrentScope();

        if (escopoAtual.verify(ctx.NOME.getText()) != null) {
            throw createSemanticException(ctx.NOME, "A variável " + ctx.NOME.getText() + " já foi declarada nesse escopo");
        } else {
            escopoAtual.insert(ctx.NOME.getText(), visitExpr(ctx.expr()));
        }

        return null;
    }

    private SemanticException createSemanticException(Token token, String msg){
        int row = token.getLine();
        int col = token.getCharPositionInLine();

        return new SemanticException("Erro semântico(" + row + ":" + col + "): " + msg);
    }
}