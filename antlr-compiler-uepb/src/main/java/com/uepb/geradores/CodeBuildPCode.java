package com.uepb.geradores;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.uepb.UEPBLanguageBaseVisitor;
import com.uepb.UEPBLanguageParser.*;
import com.uepb.semantic.Scope;

public class CodeBuildPCode extends UEPBLanguageBaseVisitor<String> implements Gerador<String>{
    
    private final Scope scopes;
    private final PCodeEndereco enderecos;
    private int label = 0;
    private String lastCode;

    public CodeBuildPCode(boolean debugMode, int MEMORY_SIZE){
        enderecos = new PCodeEndereco(MEMORY_SIZE);
        scopes = new Scope(debugMode);
        lastCode = "";
    }

    public void dropCurrentScope(){
        int[] scopeEnderecos = scopes.getCurrentScope().retrieveEnderecos();
        if(scopeEnderecos.length > 0)
            enderecos.freeEndereco(scopeEnderecos);

        scopes.dropCurrentScope();
    }

    private boolean notNull(ParserRuleContext rule){
        return rule != null;
    }

    private boolean notNull(TerminalNode node){
        return node != null;
    }

    @Override
    public String visitPrograma(ProgramaContext ctx) {
        String code = "";

        scopes.startScope();
        code += visitListaComandos(ctx.listaComandos());
        code += "stp\n";
        dropCurrentScope();

        lastCode = code;
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
        StringBuilder code = new StringBuilder("");
        scopes.getCurrentScope().insert(ctx.ID().getText(), 
            enderecos.getNextFreeEndereco());

        if(notNull(ctx.valor())){    
            int endereco = scopes
                .findEnderecoInGlobalScope(ctx.ID().getText());
            
            code.append("lda " + endereco + "\n");
            code.append(visitValor(ctx.valor()));
            code.append("sto\n");
        }

        return code.toString();
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
        String comand;

        if(notNull(ctx.NUM_INT())){
            comand = "ldc " + ctx.NUM_INT().getText() + "\n";
        }else if(notNull(ctx.NUM_REAL())){
            comand = "ldc " + ctx.NUM_REAL().getText() + "\n";
        }else if(notNull(ctx.ID())){
            int endereco = scopes.findEnderecoInGlobalScope(ctx.ID().getText());
            comand = "lod " + endereco + "\n";
        }else{
            comand = visitExprArit(ctx.exprArit());
        }

        
        if(notNull(ctx.OP_ARIT_1())){
            final String sinal = ctx.OP_ARIT_1().getText();

            if(sinal.equals("+")){
                comand = "ldc -1\nmpi\n";
            }else if(sinal.equals("-")){
                comand = "ldc 1\nmpi\n";
            }
            
        }
        
        return comand;
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

    @Override
    public String visitValor(ValorContext ctx) {
        
        if(notNull(ctx.STRING())){
            throw new RuntimeException("Strings não são suportadas na P-Code Machine");
        }else if(notNull(ctx.exprArit())){
            return visitExprArit(ctx.exprArit());
        }else if(notNull(ctx.toIntFunc())){
            return visitToIntFunc(ctx.toIntFunc());
        }else if(notNull(ctx.toFloatFunc())){
            return visitToFloatFunc(ctx.toFloatFunc());
        }else{
            return visitAskFunc(ctx.askFunc());
        }

    }

    @Override
    public String visitToIntFunc(ToIntFuncContext ctx) {
        return visitValor(ctx.valor()) + "toi\n";

    }

    @Override
    public String visitToFloatFunc(ToFloatFuncContext ctx) {
        return visitValor(ctx.valor()) + "tof\n";
        
    }

    @Override
    public String visitPrintFunc(PrintFuncContext ctx) {
        return visitValor(ctx.valor()) + "wri\n";

    }

    @Override
    public String visitAskFunc(AskFuncContext ctx) {
        return "rdi\n";
    }

    @Override
    public String visitAtribuicao(AtribuicaoContext ctx) {
        StringBuilder code = new StringBuilder();
        
        int endereco = scopes
            .findEnderecoInGlobalScope(ctx.ID().getText());
        
        code.append("lda " + endereco + "\n");
        code.append(visitValor(ctx.valor()));
        code.append("sto\n");

        return code.toString();
    }

    @Override
    public String visitIfDecl(IfDeclContext ctx) {
        final StringBuilder code = new StringBuilder();
        final int LABEL1 = this.label++;
        final int LABEL2 = this.label++;

        code.append(visitExprRel(ctx.exprRel()));
        code.append("fjp L").append(LABEL1 + "\n");
        code.append(visitEscopo(ctx.escopo()));

        if(notNull(ctx.ifTail())){
            code.append("ujp L").append(LABEL2 + "\n");
            code.append("lab L").append(LABEL1 + "\n");
            code.append(visitIfTail(ctx.ifTail()));
            code.append("lab L").append(LABEL2+ "\n");
        }

        return code.toString();
    }

    @Override
    public String visitIfTail(IfTailContext ctx) {
        return visitEscopo(ctx.escopo());
    }

    @Override
    public String visitWhileDecl(WhileDeclContext ctx) {
        final StringBuilder code = new StringBuilder();
        final int LABEL1 = this.label++;
        final int LABEL2 = this.label++;

        code.append("lab L").append(LABEL1+"\n");
        code.append(visitExprRel(ctx.exprRel()));
        code.append("fjp L").append(LABEL2+"\n");
        code.append(visitEscopoWhile(ctx.escopoWhile()));
        code.append("ujp L").append(LABEL1+"\n");
        code.append("lab L").append(LABEL2+"\n");   

        return code.toString();
    }

    @Override
    public String getGeneratedCode() {
        return lastCode;
    }

}
