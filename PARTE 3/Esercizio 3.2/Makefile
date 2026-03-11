# ============================================================
#  Esercizio 3.2 — Parser per statement e espressioni
#  Usage:
#    make              → compila
#    make run          → esegue con input.txt
#    make clean        → rimuove i file compilati
# ============================================================

JAVAC  = javac
JAVA   = java
MAIN   = Parser
INPUT  ?= input.txt

all:
	mkdir -p build
	$(JAVAC) -d build *.java

run: all
	$(JAVA) -cp build $(MAIN) $(INPUT)

clean:
	rm -rf build
	@echo "Rimossi i file compilati"

.PHONY: all run clean
