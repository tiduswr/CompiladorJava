from build.ExpressoesVisitor import ExpressoesVisitor
from build.ExpressoesParser import ExpressoesParser as ExprParser

class Calculador(ExpressoesVisitor):
    def visitPrograma(self, ctx:ExprParser.ProgramaContext):
        return self.visitExpressao(ctx.expressao())

    def visitExpressao(self, ctx:ExprParser.ExpressaoContext):
        valor = self.visitTermo(ctx.t1)

        for i in range(len(ctx.termos)):
            op1 = ctx.op1(i)
            termo2 = ctx.termos[i]

            if op1.getText() == "+":
                valor += self.visitTermo(termo2)
            else:
                valor -= self.visitTermo(termo2)
        
        return valor

    def visitTermo(self, ctx:ExprParser.TermoContext):
        valor = self.visitFator(ctx.f1)

        for i in range(len(ctx.fatores)):
            op1 = ctx.op2(i)
            fator2 = ctx.fatores[i]

            if(op1.getText() == "*"):
                valor *= self.visitFator(fator2)
            else:
                valor /= self.visitFator(fator2)
        
        return valor
    
    def visitFator(self, ctx:ExprParser.FatorContext):
        if ctx.NUM():
            return float(ctx.NUM().getText())
        else:
            return self.visitExpressao(ctx.expressao())