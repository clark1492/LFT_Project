# ============================================================
#  Esercizio 5.1 — Traduttore di espressioni in bytecode JVM
#  Usage:
#    make              → compila
#    make run          → traduce input.txt → Output.j
#    make run INPUT=altro.txt  → traduce file custom
#    make clean        → rimuove i file compilati
# ============================================================

JAVAC  = javac
JAVA   = java
INPUT  ?= input.txt

all:
	mkdir -p build
	$(JAVAC) -d build *.java

run: all
	$(JAVA) -cp build ExpressionTranslator $(INPUT)

clean:
	rm -rf build Output.j
