package com.uepb;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import com.uepb.semantic.Scope;
import com.uepb.util.PCodeEndereco;

public class CalculadoraPcode extends ExpressoesBaseVisitor<Void> {
    private List<String> code = new ArrayList<>();
    private int labelCount = 0;
    private Scope scope = new Scope();
    private PCodeEndereco enderecoManager = new PCodeEndereco();

    private String generateLabel() {
        return "L" + (labelCount++);
    }

    public List<String> generate(ParseTree tree) {
        scope.startScope();
        visit(tree);
        code.add("ldc \"O resultado é:\"");
        code.add("wri");
        code.add("wri");
        code.add("stp");
        scope.dropCurrentScope();
        return code;
    }

    @Override
    public Void visitPrograma(ExpressoesParser.ProgramaContext ctx) {
        visit(ctx.expr());
        return null;
    }

    @Override
    public Void visitExpr(ExpressoesParser.ExprContext ctx) {
        for (int i = 0; i < ctx.termo().size(); i++) {
            visit(ctx.termo(i));
            if (i > 0) {
                String op = ctx.OP1(i - 1).getText();
                if (op.equals("+")) {
                    code.add("adi");
                } else if (op.equals("-")) {
                    code.add("sbi");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitTermo(ExpressoesParser.TermoContext ctx) {
        for (int i = 0; i < ctx.fator().size(); i++) {
            visit(ctx.fator(i));
            if (i > 0) {
                String op = ctx.OP2(i - 1).getText();
                if (op.equals("*")) {
                    code.add("mpi");
                } else if (op.equals("/")) {
                    code.add("dvi");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitExpr_rel(ExpressoesParser.Expr_relContext ctx) {
        for (int i = 0; i < ctx.termo_rel().size(); i++) {
            visit(ctx.termo_rel(i));
            if (i > 0) {
                String op = ctx.OP_BOOL(i - 1).getText();
                if (op.equals("and")) {
                    code.add("and");
                } else if (op.equals("or")) {
                    code.add("or");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitTermo_rel(ExpressoesParser.Termo_relContext ctx) {
        if (ctx.getChildCount() == 3) {
            visit(ctx.fator_rel(0));
            visit(ctx.fator_rel(1));
            String op = ctx.OP_REL().getText();
            switch (op) {
                case ">":
                    code.add("grt");
                    break;
                case ">=":
                    code.add("gte");
                    break;
                case "<":
                    code.add("let");
                    break;
                case "<=":
                    code.add("lte");
                    break;
                case "==":
                    code.add("equ");
                    break;
                case "!=":
                    code.add("neq");
                    break;
            }
        } else {
            visit(ctx.expr_rel());
        }
        return null;
    }

    @Override
    public Void visitFator_rel(ExpressoesParser.Fator_relContext ctx) {
        if (ctx.VAR != null) {
            Integer endereco = scope.findEnderecoInGlobalScope(ctx.VAR.getText());
            if (endereco == null) {
                throw new RuntimeException("A variável " + ctx.VAR.getText() + " não foi declarada.");
            }
            code.add("lod " + endereco);
        } else if (ctx.CONST != null) {
            code.add("ldc " + ctx.CONST.getText());
        } else if (ctx.expr() != null) {
            visit(ctx.expr());
        }
        return null;
    }

    @Override
    public Void visitFator(ExpressoesParser.FatorContext ctx) {
        if (ctx.EXPR_PARENTESES != null) {
            visit(ctx.EXPR_PARENTESES);
        } else if (ctx.VAR != null) {
            Integer endereco = scope.findEnderecoInGlobalScope(ctx.VAR.getText());
            if (endereco == null) {
                throw new RuntimeException("A variável " + ctx.VAR.getText() + " não foi declarada.");
            }
            code.add("lod " + endereco);
        } else if (ctx.CONST != null) {
            code.add("ldc " + ctx.CONST.getText());
        } else if (ctx.expr_rel() != null) {
            visit(ctx.expr_rel());
            String elseLabel = generateLabel();
            String endLabel = generateLabel();

            code.add("fjp " + elseLabel);
            visit(ctx.IF_TRUE);            
            code.add("ujp " + endLabel);

            code.add("lab " + elseLabel);
            visit(ctx.IF_FALSE);

            code.add("lab " + endLabel);
        } else if (ctx.getText().startsWith("let")) {
            scope.startScope();
            visit(ctx.listaDeclaracoes());
            visit(ctx.SUB_EXPR);
            enderecoManager.freeEndereco(scope.getCurrentScope().retrieveEnderecos());
            scope.dropCurrentScope();            
        }
        return null;
    }

    @Override
    public Void visitListaDeclaracoes(ExpressoesParser.ListaDeclaracoesContext ctx) {
        for (ExpressoesParser.DeclaracaoContext decl : ctx.declaracao()) {
            visit(decl);
        }
        return null;
    }

    @Override
    public Void visitDeclaracao(ExpressoesParser.DeclaracaoContext ctx) {
        String varName = ctx.NOME.getText();

        Integer endereco = scope.getCurrentScope().retrieveEndereco(varName);
        if (endereco != null) {
            throw new RuntimeException("A variável " + varName + " já foi declarada.");
        }

        endereco = enderecoManager.getNextFreeEndereco();
        code.add("lda " + endereco);
        visit(ctx.expr());        
        code.add("sto");
        scope.getCurrentScope().insertWithEndereco(varName, endereco);
        return null;
    }

}