import sys
from antlr4 import *
from build.ExpressoesLexer import ExpressoesLexer as ExprLexer
from build.ExpressoesParser import ExpressoesParser as ExprParser
from Calculador import Calculador

def main(argv):
    input_stream = FileStream(argv[1])
    lexer = ExprLexer(input_stream)
    stream = CommonTokenStream(lexer)
    parser = ExprParser(stream)
    tree = parser.programa()

    calculador = Calculador()
    val = calculador.visitPrograma(tree)

    print(f"Valor da expressão: {val}")

if __name__ == '__main__':
    main(sys.argv)