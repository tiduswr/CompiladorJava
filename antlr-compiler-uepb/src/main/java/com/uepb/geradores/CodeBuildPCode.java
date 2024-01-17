package com.uepb.geradores;

import org.antlr.v4.parse.ANTLRParser.range_return;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.uepb.UEPBLanguageBaseVisitor;
import com.uepb.UEPBLanguageParser.AtribuicaoContext;
import com.uepb.UEPBLanguageParser.ComandoContext;
import com.uepb.UEPBLanguageParser.DeclaracaoContext;
import com.uepb.UEPBLanguageParser.EscopoContext;
import com.uepb.UEPBLanguageParser.EscopoWhileContext;
import com.uepb.UEPBLanguageParser.ExprAritContext;
import com.uepb.UEPBLanguageParser.ExprRelContext;
import com.uepb.UEPBLanguageParser.FatorAritContext;
import com.uepb.UEPBLanguageParser.ListaComandosContext;
import com.uepb.UEPBLanguageParser.ProgramaContext;
import com.uepb.UEPBLanguageParser.TermoAritContext;
import com.uepb.UEPBLanguageParser.TermoRelContext;
import com.uepb.UEPBLanguageParser.ValorContext;
import com.uepb.semantic.Scope;

public class CodeBuildPCode extends UEPBLanguageBaseVisitor<String>{
    
    private final Scope scopes;
    private final PCodeEndereco enderecos;
    private int label = 0;

    public CodeBuildPCode(boolean debugMode, int MEMORY_SIZE){
        enderecos = new PCodeEndereco(MEMORY_SIZE);
        scopes = new Scope(debugMode);
    }

    public void dropCurrentScope(){
        int[] scopeEnderecos = scopes.getCurrentScope().retrieveEnderecos();
        if(scopeEnderecos.length > 0)
            enderecos.freeEndereco(scopeEnderecos);

        scopes.dropCurrentScope();
    }

    @Override
    public String visitPrograma(ProgramaContext ctx) {
        String code = "";

        scopes.startScope();
        code += visitListaComandos(ctx.listaComandos());
        code += "stp\n";
        dropCurrentScope();

        return code;
    }

    @Override
    public String visitEscopo(EscopoContext ctx) {
        scopes.startScope();
        var code = visitListaComandos(ctx.listaComandos());
        dropCurrentScope();

        return code;
    }

    @Override
    public String visitEscopoWhile(EscopoWhileContext ctx) {
        scopes.startScope();
        
        StringBuilder code = new StringBuilder();
        ctx.comando()
            .forEach(comando -> code.append(visitComando(comando)));
        ctx.whileBreak()
            .forEach(wb -> code.append(visitWhileBreak(wb)));

        dropCurrentScope();

        return code.toString();
    }

    @Override
    public String visitListaComandos(ListaComandosContext ctx) {
        StringBuilder code = new StringBuilder();

        ctx.comando()
            .forEach(comando -> code.append(visitComando(comando)));

        return code.toString();
    }

    private boolean notNull(ParserRuleContext rule){
        return rule != null;
    }

    private boolean notNull(TerminalNode node){
        return node != null;
    }

    @Override
    public String visitComando(ComandoContext ctx) {

        if(notNull(ctx.declaracao())){
            return visitDeclaracao(ctx.declaracao());

        }else if(notNull(ctx.atribuicao())){
            return visitAtribuicao(ctx.atribuicao());

        }else if(notNull(ctx.ifDecl())){
            return visitIfDecl(ctx.ifDecl());

        }else if(notNull(ctx.whileDecl())){
            return visitWhileDecl(ctx.whileDecl());

        }else if(notNull(ctx.printFunc())){
            return visitPrintFunc(ctx.printFunc());

        }else if(notNull(ctx.askFunc())){
            return visitAskFunc(ctx.askFunc());
        }

        return null;
    }

    @Override
    public String visitDeclaracao(DeclaracaoContext ctx) {
        scopes.getCurrentScope().insert(ctx.ID().getText(), 
            enderecos.getNextFreeEndereco());
        return null;
    }

    @Override
    public String visitExprArit(ExprAritContext ctx) {
        StringBuilder code = new StringBuilder();

        var termo = ctx.termoArit(0);
        code.append(visitTermoArit(termo));

        for(int i = 1; i < ctx.termoArit().size(); i++){
            var operator = ctx.OP_ARIT_1(i - 1).getText();
            termo = ctx.termoArit(i);

            code.append(visitTermoArit(termo));

            if(operator.equals("+")){
                code.append("adi\n");
            }else if(operator.equals("-")){
                code.append("sbi\n");
            }
        }

        return code.toString();
    }

    @Override
    public String visitTermoArit(TermoAritContext ctx) {
        StringBuilder code = new StringBuilder();

        var fator = ctx.fatorArit(0);
        code.append(visitFatorArit(fator));

        for(int i = 1; i < ctx.fatorArit().size(); i++){
            var operator = ctx.OP_ARIT_2(i - 1).getText();
            fator = ctx.fatorArit(i);

            code.append(visitFatorArit(fator));

            if(operator.equals("*")){
                code.append("mpi\n");
            }else if(operator.equals("/")){
                code.append("dvi\n");
            }
        }

        return code.toString();
    }

    @Override
    public String visitFatorArit(FatorAritContext ctx) {
        if(notNull(ctx.NUM_INT())){
            return "ldc " + ctx.NUM_INT().getText() + "\n";
        }else if(notNull(ctx.NUM_REAL())){
            return "ldc" + ctx.NUM_REAL().getText() + "\n";
        }else if(notNull(ctx.ID())){
            int endereco = scopes.findEnderecoInGlobalScope(ctx.ID().getText());
            return "lod " + endereco + "\n";
        }else{
            return visitExprArit(ctx.exprArit());
        }
    }

    @Override
    public String visitExprRel(ExprRelContext ctx) {
        StringBuilder code = new StringBuilder();

        var termo = ctx.termoRel(0);
        code.append(visitTermoRel(termo));
        for(int i = 1; i < ctx.termoRel().size(); i++){
            var operator = ctx.OP_BOOL(i-1).getText();
            termo = ctx.termoRel(i);

            code.append(visitTermoRel(termo));

            if(operator.equals("and")){
                code.append("and\n");
            }else if(operator.equals("or")){
                code.append("or\n");
            }
        }

        return code.toString();
    }

    @Override
    public String visitTermoRel(TermoRelContext ctx) {
        if(notNull(ctx.exprRel())){
            return visitExprRel(ctx.exprRel());
        }else{
            String code = visitValor(ctx.v1) + visitValor(ctx.v2);

            switch(ctx.OP_REL().getText()){

                case ">":
                    return code += "grt\n";
                case ">=":
                    return code += "gte\n";
                case "<":
                    return code += "let\n";
                case "<=":
                    return code += "gte\n";
                case "!=":
                    return code += "neq\n";
                case "==":
                    return code += "equ\n";
                default:
                    return null;

            }

        }
    }

}
