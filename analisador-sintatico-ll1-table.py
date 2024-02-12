from collections import deque

def parse(w, table, start):
    indice = 0
    stack = deque(['$', start])
    
    while stack[-1] != '$':
        print(f">> Pilha: {''.join(reversed(stack))} Entrada: {''.join(w[indice:])}")
        X = stack[-1]
        a = w[indice]
        
        if X == a:
            print(f"Match {X} == {a}")
            stack.pop()
            indice += 1 
        elif X.islower():
            print("Erro: Não é possível desempilhar terminal")
            return
        elif not _checkTable(table, X, a, stack):
            return
        
    print(f">> Pilha: {''.join(reversed(stack))} Entrada: {''.join(w[indice:])}")
    print('Match $ == $ -> Cadeia reconhecida!')

def _checkTable(table, X, a, stack):
    try:
        production = table[X, a]
    except KeyError:
        print("Erro: Entrada não está na tabela")
        return False

    if not production:
        print("Erro: Produção vazia")
        return False

    print(f"{X} → {''.join(production)}")
    stack.pop()

    if production[0] != 'ε':
        for symbol in reversed(production):
            stack.append(symbol)
    
    return True

# Tabela de análise sintática
TABLE = {
    ('E', '('): ['T', 'E\''],
    ('E', 'id'): ['T', 'E\''],
    ('E\'', '+'): ['+', 'T', 'E\''],
    ('E\'', ')'): ['ε'],
    ('E\'', '$'): ['ε'],
    ('T', '('): ['F', 'T\''],
    ('T', 'id'): ['F', 'T\''],
    ('T\'', '+'): ['ε'],
    ('T\'', ')'): ['ε'],
    ('T\'', '$'): ['ε'],
    ('T\'', '*'): ['*', 'F', 'T\''],
    ('F', '('): ['(','E', ')'],
    ('F', 'id'): ['id'],
}

entrada = ['id','+','id','*','id','$']
parse(entrada, TABLE, 'E')