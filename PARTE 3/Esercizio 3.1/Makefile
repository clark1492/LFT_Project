# ============================================================
#  Esercizio 3.1 — Parser per espressioni aritmetiche
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
	java -cp classes Parser $(INPUT)

clean:
	rm -rf classes

