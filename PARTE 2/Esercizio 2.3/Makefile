# ============================================================
#  Esercizio 2.3 — Lexer con commenti
#  Usage:
#    make        → compila
#    make run    → esegue con prova.txt
#    make clean  → rimuove i file compilati
# ============================================================

INPUT  ?= input.txt

all:
	mkdir -p classes
	javac -d classes *.java

run: all
	java -cp classes Lexer $(INPUT)

clean:
	rm -rf classes

.PHONY: all run clean
