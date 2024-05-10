## Gramatica
```g4
grammar Expressoes;

programa
:
	expr
;

expr:
	TERMO=termo (OP1 TERMOS+=termo)*
;

termo:
	FATOR=fator (OP2 FATORES+=fator)*
;

fator: 
	'(' EXPR_PARENTESES=expr ')'
	| VAR=ID
	| CONST=NUM
	| 'let' listaDeclaracoes '->' SUB_EXPR=expr
;

listaDeclaracoes:
	declaracao (',' declaracao)*
;

declaracao:
	NOME=ID '=' expr
;

NUM: [0-9]+ ('.' [0-9]+)?;
OP1: '+' | '-';
OP2: '*' | '/';
ID: ID_START (ID_START | [0-9])*;
ID_START: ([a-z] | [A-Z] | '_');
WS: [ \t\r\n]+ -> skip;
```

## Scope
```java
package com.uepb;

import java.util.LinkedList;
import java.util.List;

public class Scope {
    private LinkedList<SymbolTable> stack;

    public Scope(){
        stack = new LinkedList<>();
        startScope();
    }

    public void startScope() {
        stack.push(new SymbolTable());
    }

    public SymbolTable getCurrentScope(){
        return stack.peek();
    }

    public void dropCurrentScope(){
        stack.pop();
    }

    public List<SymbolTable> getAllSymbolTable(){
        return stack;
    }

}
```
## Symbol Table
```java
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    
    public record TableInput(String nome, double valor){}

    private final Map<String, TableInput> table;

    public SymbolTable(){
        table = new HashMap<>();
    }

    public void insert(String symbolName, double symbolValue){
        table.put(symbolName, new TableInput(symbolName, symbolValue));
    }

    public TableInput verify(String symbolName){
        return table.get(symbolName);
    }

}
```

## Semantic Exception
```java
public class SemanticException extends RuntimeException{
    
    public SemanticException(String message){
        super(message);
    }

}
```

## Calculadora
```java
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
```

## Main
```java
import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

public class Main {
    public static void main(String[] args) throws IOException {
        CharStream charStream = CharStreams.fromFileName(args[0]);
        ExpressoesLexer lexer = new ExpressoesLexer(charStream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressoesParser parser = new ExpressoesParser(tokenStream);
        
        Calculadora calculadora = new Calculadora();
        var tree = parser.programa();
        var valor = calculadora.visitPrograma(tree);

        System.out.println("Valor do cálculo: " + valor);
    }
}
```