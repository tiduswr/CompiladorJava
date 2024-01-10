import sys
from antlr4 import *
from build.ExprLexer import ExprLexer
from build.ExprParser import ExprParser

def main(argv):
    input_stream = FileStream(argv[1])
    lexer = ExprLexer(input_stream)
    stream = CommonTokenStream(lexer)
    parser = ExprParser(stream)
    val = parser.programa().val

    print(f"Valor da expressão: {val}")

if __name__ == '__main__':
    main(sys.argv)