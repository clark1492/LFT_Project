# ============================================================
#  Esercizio 2.2 — Lexer con identificatori estesi
#  Usage:
#    make        → compila
#    make run    → esegue con prova.txt
#    make clean  → rimuove i file .class
# ============================================================

INPUT  ?= input.txt

all:
	mkdir -p classes
	javac -d classes *.java

run: all
	java -cp classes Lexer $(INPUT)

clean:
	rm -rf classes
