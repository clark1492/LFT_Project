# ============================================================
#  Esercizio 5.2 — Traduttore di programmi in bytecode JVM
#  Usage:
#    make              → compila
#    make run          → traduce input.txt → Output.j
#    make run INPUT=altro.txt  → traduce file custom
#    make clean        → rimuove i file compilati e Output.j
# ============================================================

JAVAC  = javac
JAVA   = java
INPUT  ?= input.txt

all:
	mkdir -p build
	$(JAVAC) -d build *.java

run: all
	$(JAVA) -cp build Translator $(INPUT)

clean:
	rm -rf build Output.j

