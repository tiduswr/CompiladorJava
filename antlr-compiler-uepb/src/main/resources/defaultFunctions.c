#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_INPUT_SIZE 255

char* ask(const char* message) {
    char* input = (char*)malloc(MAX_INPUT_SIZE * sizeof(char));

    if (input == NULL) {
        fprintf(stderr, "Erro ao ler entrada.\n");
        exit(1);
    }

    printf("%s", message);

    if (fgets(input, MAX_INPUT_SIZE, stdin) == NULL) {
        free(input);
        fprintf(stderr, "Erro ao ler a entrada.\n");
        exit(1);
    }
    
    if (input[strlen(input) - 1] == '\n') {
        input[strlen(input) - 1] = '\0';
    }

    return input;
}

char* createString(const char* entrada) {
    size_t tamanhoEntrada = strlen(entrada);

    char* novaString = (char*)malloc((tamanhoEntrada + 1) * sizeof(char));

    if (novaString == NULL) {
        fprintf(stderr, "Erro ao alocar memória.\n");
        exit(1);
    }

    strcpy(novaString, entrada);

    return novaString;
}

