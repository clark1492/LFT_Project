# Esercizio 5.1 — Traduttore di espressioni in bytecode JVM

## Descrizione

Estensione dell'Esercizio 4.1. Invece di calcolare il valore dell'espressione direttamente, il programma **traduce** l'espressione in **bytecode JVM** in formato Jasmin (`.j`), che può essere poi assemblato ed eseguito sulla JVM.

## Differenze rispetto all'Esercizio 4.1

| Funzionalità                        | Es. 4.1     | Es. 5.1          |
|-------------------------------------|-------------|------------------|
| Verifica sintattica                 | ✅ Parser   | ✅               |
| Calcolo del valore                  | ✅ Valutatore | ❌             |
| Generazione bytecode JVM (Jasmin)   | ❌          | ✅               |
| Output file `.j`                    | ❌          | ✅ `Output.j`    |
| Supporto `print(expr)`              | ❌          | ✅               |

## Grammatica riconosciuta

```
prog   → print ( expr ) EOF
expr   → term exprp
exprp  → + term { emit(iadd) } exprp
       | - term { emit(isub) } exprp
       | ε
term   → fact termp
termp  → * fact { emit(imul) } termp
       | / fact { emit(idiv) } termp
       | ε
fact   → ( expr )
       | NUM  { emit(ldc, NUM.val) }
```

Le istruzioni bytecode vengono emesse **durante il parsing** (traduzione guidata dalla sintassi).

## Istruzioni bytecode generate

| Operazione | Istruzione JVM |
|------------|----------------|
| Numero `n` | `ldc n`        |
| Addizione  | `iadd`         |
| Sottrazione| `isub`         |
| Moltiplicazione | `imul`    |
| Divisione  | `idiv`         |
| Stampa     | `invokestatic Output/print(I)V` |

## Struttura dei file

```
Esercizio 5.1/
├── ExpressionTranslator.java  # traduttore, contiene il main
├── CodeGenerator.java         # genera e scrive il file Jasmin
├── Instruction.java           # rappresenta una singola istruzione
├── OpCode.java                # enum dei codici operativi JVM
├── Parser.java                # parser sintattico (da Es. 3.1)
├── Valutatore.java            # valutatore (da Es. 4.1)
├── Lexer.java                 # analizzatore lessicale
├── Token.java                 # classe base per i token
├── Word.java                  # token per parole chiave
├── NumberTok.java             # token per i numeri
├── Tag.java                   # costanti per i tipi di token
└── input.txt                  # file di input di esempio
```

## Compilazione ed esecuzione

Dalla cartella `Esercizio 5.1/`:

```bash
# Compilazione
mkdir -p build
javac -d build *.java

# Traduzione (genera Output.j)
java -cp build ExpressionTranslator input.txt
```

## Esempio

Input (`input.txt`):
```
print(3 + 4 * 2)
```

Output (`Output.j`):
```jasmin
.class public Output
.super java/lang/Object
...
.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 3
 ldc 4
 ldc 2
 imul
 iadd
 invokestatic Output/print(I)V
 return
.end method
```

## Note

- Il file di input deve contenere **una sola istruzione** `print(expr)`
- Il file `Output.j` viene generato nella stessa cartella di esecuzione
- Per eseguire il bytecode generato è necessario **Jasmin** (`jasmin Output.j`) e poi `java Output`

## Struttura del codice

```
ExpressionTranslator
├── prog()    # punto di ingresso: riconosce print(expr) ed emette invokestatic
├── expr()    # traduce somme/sottrazioni
├── exprp()   # continuazione di expr, emette iadd/isub
├── term()    # traduce moltiplicazioni/divisioni
├── termp()   # continuazione di term, emette imul/idiv
├── fact()    # emette ldc per i numeri, ricorre per le parentesi
├── move()    # avanza al prossimo token
├── match()   # verifica e consuma un token
├── error()   # segnala errore sintattico
└── main()    # legge il file e avvia la traduzione

CodeGenerator
├── emit()        # aggiunge un'istruzione alla lista
├── toJasmin()    # scrive il file Output.j completo
└── newLabel()    # genera un nuovo label univoco
```
