# ============================================================
#  Esercizio 4.1 — Parser e Valutatore di espressioni
#  Usage:
#    make              → compila
#    make run          → esegue il Parser con input.txt
#    make eval         → esegue il Valutatore con input.txt
#    make run INPUT=altro.txt   → Parser con file custom
#    make eval INPUT=altro.txt  → Valutatore con file custom
#    make clean        → rimuove i file compilati
# ============================================================

JAVAC  = javac
JAVA   = java
INPUT  ?= input.txt

all:
	mkdir -p build
	$(JAVAC) -d build *.java

run: all
	$(JAVA) -cp build Parser $(INPUT)

eval: all
	$(JAVA) -cp build Valutatore $(INPUT)

clean:
	rm -rf build
