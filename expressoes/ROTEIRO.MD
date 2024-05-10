## Gramática
```g4
grammar Expressoes;

programa:
	expressao EOF
;

expressao:
	t1=termo (op1 termos+=termo)*
;

termo: 
	f1=fator (op2 fatores+=fator)*
;

fator:
	CS_ABRE_PAR expressao CS_FECHA_PAR 
	| NUM
;

op1: 
	OP_SUM | OP_SUB
;

op2: 
	OP_DIV | OP_MUL
;

OP_SUM: [+];
OP_MUL: [*];
OP_DIV: [/];
OP_SUB: [-];
CS_ABRE_PAR: [(];
CS_FECHA_PAR: [)];
NUM: [0-9]+([.][0-9]+)?;
WS: [ \t\r\n] -> skip;
```
## Calculadora
```java
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

            if(op1.OP_SUM() != null){
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

            if(op2.OP_MUL() != null){
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
```

## Main
```java
import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

public class Main {

    public static void main(String[] args) throws IOException {
        CharStream charStream = CharStreams.fromFileName(args[0]);
        ExpressoesLexer lexer = new ExpressoesLexer(charStream);

        Token t = null;
        while((t=lexer.nextToken()).getType() != Token.EOF){
            System.out.println("<"+ExpressoesLexer.VOCABULARY.getSymbolicName(t.getType())+", "+t.getText()+">");
        }
        lexer.reset();

        TokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressoesParser parser = new ExpressoesParser(tokenStream);
        var arvore = parser.programa();
        Calculador calculador = new Calculador();
        var valor = calculador.visitPrograma(arvore);

        System.out.println("Valor do programa.expr: " + valor);
    }
}
```