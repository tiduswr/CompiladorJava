package com.uepb.semantic;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import com.uepb.UEPBLanguageParser.ExprAritContext;
import com.uepb.UEPBLanguageParser.FatorAritContext;
import com.uepb.UEPBLanguageParser.TermoAritContext;
import com.uepb.UEPBLanguageParser.ValorContext;

import static com.uepb.semantic.SymbolTable.UEPBLanguageType;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.INVALIDO;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.INTEIRO;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.FLOAT;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.STRING;

public class Utils {
    
    public static final List<String> semanticErrors = new ArrayList<>();

    public static void insertSemanticError(Token t, String message){
        int line = t.getLine();
        int col = t.getCharPositionInLine();

        semanticErrors.add(String.format("Erro %d:%d: %s", line, col, message));
    }

    public static UEPBLanguageType verifyType(Scope scope, ValorContext ctx){
        if(ctx.STRING() != null || ctx.askFunc() != null){
            return STRING;
        }else{
            return verifyType(scope, ctx.exprArit());
        }
    }

    public static UEPBLanguageType verifyType(Scope scope, ExprAritContext ctx){

        UEPBLanguageType retorno = null;
        for(var TERMO : ctx.termoArit()){
            UEPBLanguageType aux = verifyType(scope, TERMO);

            if(retorno == null){
                retorno = aux;
            }else if(retorno != aux && aux != INVALIDO){
                insertSemanticError(ctx.start, "A expressão " + ctx.getText() + " contém tipos inválidos");
                retorno = INVALIDO;
            }
        }

        return retorno;
    }

    public static UEPBLanguageType verifyType(Scope scope, TermoAritContext ctx) {
        
        UEPBLanguageType retorno = null;
        for(var FATOR : ctx.fatorArit()){
            UEPBLanguageType aux = verifyType(scope, FATOR);

            if(retorno == null){
                retorno = aux;
            }else if(retorno != aux && aux != INVALIDO){
                insertSemanticError(ctx.start, "A expressão " + ctx.getText() + " contém tipos inválidos");
                retorno = INVALIDO;
            }
        }

        return retorno;
    }

    public static UEPBLanguageType verifyType(Scope scope, FatorAritContext ctx) {
        
        if(ctx.NUM_INT() != null){
            return INTEIRO;
        }else if(ctx.NUM_REAL() != null){
            return FLOAT;
        }else if(ctx.exprArit() != null){
            return verifyType(scope, ctx.exprArit());
        }else{
            String varName = ctx.ID().getText();

            var allSymbols = scope.getAllSymbolTable();

            if(!allSymbols.stream().anyMatch(t -> t.exists(varName))){
                insertSemanticError(ctx.ID().getSymbol(), "A variável " + ctx.getText() + " não foi declarada");
                return INVALIDO;
            }

            return verifyType(allSymbols, varName);
        }

    }

    public static UEPBLanguageType verifyType(List<SymbolTable> allSymbols, String varName) {
        return allSymbols.stream()
            .map(table -> table.verify(varName))
            .filter(type -> type != null)
            .findFirst()
            .get(); // Seguro, pois a regra do contexto de FATOR verifica se existe ao menos 1
    }

}
