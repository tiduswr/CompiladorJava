package com.uepb.semantic;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import com.uepb.UEPBLanguageParser.ExprAritContext;
import com.uepb.UEPBLanguageParser.FatorAritContext;
import com.uepb.UEPBLanguageParser.TermoAritContext;
import com.uepb.UEPBLanguageParser.TermoRelContext;
import com.uepb.UEPBLanguageParser.ValorContext;

import static com.uepb.semantic.SymbolTable.UEPBLanguageType;
import static com.uepb.semantic.SymbolTable.UEPBLanguageType.*;

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
        } else if(ctx.toFloatFunc() != null){
            return FLOAT;
        } else if(ctx.toIntFunc() != null){
            return INTEIRO;
        } else {
            return verifyType(scope, ctx.exprArit());
        }
    }

    public static UEPBLanguageType verifyType(Scope scope, ExprAritContext ctx){
        return ctx.termoArit().stream()
            .map(termo -> verifyType(scope, termo))
            .reduce(null, (acc, curr) -> {
                if (acc == null) return curr;
                if (acc != curr && curr != INVALIDO) {
                    insertSemanticError(ctx.start, "A expressão " + ctx.getText() + " contém tipos inválidos");
                    return INVALIDO;
                }
                return acc;
            });
    }

    public static UEPBLanguageType verifyType(Scope scope, TermoAritContext ctx) {
        return ctx.fatorArit().stream()
            .map(fator -> verifyType(scope, fator))
            .reduce(null, (acc, curr) -> {
                if (acc == null) return curr;
                if (acc != curr && curr != INVALIDO) {
                    insertSemanticError(ctx.start, "A expressão " + ctx.getText() + " contém tipos inválidos");
                    return INVALIDO;
                }
                return acc;
            });
    }

    public static UEPBLanguageType verifyType(Scope scope, FatorAritContext ctx) {
        if(ctx.NUM_INT() != null){
            return INTEIRO;
        } else if(ctx.NUM_REAL() != null){
            return FLOAT;
        } else if(ctx.exprArit() != null){
            return verifyType(scope, ctx.exprArit());
        } else {
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
            .get();
    }

    public static UEPBLanguageType verifyType(Scope scopes, TermoRelContext termoRel) {
        var firstValor = Utils.verifyType(scopes, termoRel.v1);
        var secondValor = Utils.verifyType(scopes, termoRel.v2);
        if((firstValor != secondValor) || (firstValor == STRING || secondValor == STRING))
            Utils.insertSemanticError(termoRel.OP_REL().getSymbol(), "A expressão relacional " 
            + termoRel.getText() + " precisa ser entre elementos do mesmo tipo e não ser uma STRING");
        return null;
    }
}