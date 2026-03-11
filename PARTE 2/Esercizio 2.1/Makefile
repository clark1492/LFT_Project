# ============================================================
#  Esercizio 2.1 — Lexer
#  Usage:
#    make        → compila
#    make run    → esegue con input.txt
#    make clean  → rimuove i file .class
# ============================================================

INPUT  ?= "input.txt"

all:
	mkdir -p classes
	javac -d classes *.java

run: all
	java -cp classes Lexer $(INPUT)

clean:
	rm -rf classes
