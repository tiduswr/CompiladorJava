# Compilador Java | UEPB

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

## Descrição do Projeto

O Compilador UEPB é um projeto que visa criar um compilador para uma linguagem simples com fins acadêmicos. Este compilador realiza as etapas fundamentais, como análise léxica, análise sintática, geração de código intermediário e geração de código final.

## Requisitos

- Java 17
- Maven

## Instalação

```bash
$ git clone https://github.com/tiduswr/CompiladorJava.git
$ cd CompiladorJava
$ mvn package
$ java -jar target/compiler.java code_example.uepb
