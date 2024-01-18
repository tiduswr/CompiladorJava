# P-Code Machine

Baseado no projeto do Professor Dr. Daniel Lucrédio da UFSCar, disponibilizado no [GitHub](https://github.com/dlucredio/cursocompiladores/tree/master/pCodeMachine)

## Instruções

As seguintes instruções são aceitas pela P-Code Machine:

| Instrução | Descrição |
|-----------|-----------|
| `rdi`     | Retira um endereço `A` do topo da pilha, lê um valor `X` da entrada e armazena `X` na memória, no endereço `A` |
| `wri`     | Retira um valor `X` do topo da pilha e escreve `X` na saída |
| `lda A`   | Insere o endereço `A` no topo da pilha |
| `ldc C`   | Insere a constante `C` no topo da pilha |
| `lod A`   | Lê o conteúdo da memória no endereço `A` e o insere no topo da pilha |
| `mpi`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e insere `X * Y` no topo da pilha |
| `dvi`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e insere `Y / X` no topo da pilha |
| `adi`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e insere `X + Y` no topo da pilha |
| `sbi`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e insere `Y - X` no topo da pilha |
| `sto`     | Retira um valor `X` do topo da pilha, retira um endereço `A` do topo da pilha, e armazena `X` na memória, no endereço `A` |
| `grt`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e armazena `Y > X` (um booleano) no topo da pilha |
| `let`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e armazena `Y < X` (um booleano) no topo da pilha |
| `gte`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e armazena `Y >= X` (um booleano) no topo da pilha |
| `lte`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e armazena `Y <= X` (um booleano) no topo da pilha |
| `equ`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e armazena `X == Y` (um booleano) no topo da pilha |
| `neq`     | Retira um valor `X` do topo da pilha, retira outro valor `Y` do topo da pilha, e armazena `X != Y` (um booleano) no topo da pilha |
| `and`     | Retira um valor booleano `X` do topo da pilha, retira outro valor booleano `Y` do topo da pilha, e armazena `X && Y` (um booleano) no topo da pilha |
| `or`      | Retira um valor booleano `X` do topo da pilha, retira outro valor booleano `Y` do topo da pilha, e armazena `X || Y` (um booleano) no topo da pilha |
| `toi`      | Retira um valor float `X` do topo da pilha e insere `X` transformado em inteiro no topo da pilha, similar a FIST (Floating Point Integer Store) no Assembly x86 |
| `tof`      | Retira um valor int `X` do topo da pilha e insere `X` transformado em float no topo da pilha, similar a FILD (Floating Point Integer Load) no Assembly x86 |
| `lab L`   | Sem efeito na execução. Apenas marca uma posição de código com um rótulo `L` |
| `ujp L`   | Salta para a instrução marcada com `L` |
| `fjp L`   | Retira um valor booleano `X` do topo da pilha e caso seja falso, salta para a instrução marcada com `L`. Caso seja verdadeiro, não executa nada. |
| `stp`     | Interrompe a execução |
