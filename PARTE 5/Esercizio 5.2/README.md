# Esercizio 5.2 — Traduttore di programmi completi in bytecode JVM

## Descrizione

Estensione dell'Esercizio 5.1. Il traduttore ora gestisce **programmi completi** con variabili, assegnamenti, I/O, condizionali e cicli, generando bytecode JVM in formato Jasmin (`Output.j`). Introduce la **symbol table** per gestire le variabili e i **label** per il controllo del flusso.

## Differenze rispetto all'Esercizio 5.1

| Funzionalità                        | Es. 5.1              | Es. 5.2              |
|-------------------------------------|----------------------|----------------------|
| Solo `print(expr)`                  | ✅                   | ❌ (esteso)          |
| Variabili e assegnamenti            | ❌                   | ✅                   |
| `read(id)`                          | ❌                   | ✅                   |
| `if bexpr then stat [else stat]`    | ❌                   | ✅                   |
| `for (id = expr; bexpr) do stat`    | ❌                   | ✅                   |
| Blocchi `begin ... end`             | ❌                   | ✅                   |
| Symbol table per le variabili       | ❌                   | ✅                   |
| Label per il controllo del flusso   | ❌                   | ✅                   |

## Grammatica riconosciuta

```
prog      → statlist EOF
statlist  → stat statlistp
statlistp → ; stat statlistp | ε
stat      → id = expr                          { istore addr }
           | print ( expr )                    { invokestatic print }
           | read ( id )                       { invokestatic read; istore addr }
           | if bexpr then stat [ else stat ]  { salti condizionali }
           | for ( id = expr ; bexpr ) do stat { loop con label }
           | begin statlist end
bexpr     → expr RELOP expr                   { if_icmpXX ltrue; goto lfalse }
expr      → term exprp
exprp     → + term { iadd } exprp | - term { isub } exprp | ε
term      → fact termp
termp     → * fact { imul } termp | / fact { idiv } termp | ε
fact      → ( expr ) | NUM { ldc } | ID { iload addr }
```

## Istruzioni bytecode generate

| Operazione        | Istruzione JVM                     |
|-------------------|------------------------------------|
| Numero `n`        | `ldc n`                            |
| Variabile `x`     | `iload addr`                       |
| Assegnamento      | `istore addr`                      |
| Addizione         | `iadd`                             |
| Sottrazione       | `isub`                             |
| Moltiplicazione   | `imul`                             |
| Divisione         | `idiv`                             |
| Stampa            | `invokestatic Output/print(I)V`    |
| Lettura           | `invokestatic Output/read()I`      |
| Salto condizionale| `if_icmpeq/lt/le/gt/ge/ne ltrue`   |
| Salto incondizionato | `goto label`                    |

## Struttura dei file

```
Esercizio 5.2/
├── Translator.java          # traduttore completo, contiene il main
├── SymbolTable.java         # tabella dei simboli per le variabili
├── CodeGenerator.java       # genera e scrive il file Jasmin
├── Instruction.java         # rappresenta una singola istruzione
├── OpCode.java              # enum dei codici operativi JVM
├── ExpressionTranslator.java # traduttore di sole espressioni (da Es. 5.1)
├── Parser.java              # parser sintattico
├── Valutatore.java          # valutatore
├── Lexer.java               # analizzatore lessicale
├── Token.java               # classe base per i token
├── Word.java                # token per parole chiave
├── NumberTok.java           # token per i numeri
├── Tag.java                 # costanti per i tipi di token
└── input.txt                # file di input di esempio
```

## Compilazione ed esecuzione

Dalla cartella `Esercizio 5.2/`:

```bash
# Compilazione
mkdir -p build
javac -d build *.java

# Traduzione (genera Output.j)
java -cp build Translator input.txt
```

## Esempio

Input (`input.txt`):
```
read(n);
x = 10;
if n > 0 then print(n + x) else print(0)
```

Output (`Output.j`) — estratto:
```jasmin
.method public static run()V
 .limit stack 1024
 .limit locals 256
 invokestatic Output/read()I
 istore 0
 ldc 10
 istore 1
 iload 0
 ldc 0
 if_icmpgt L2
 goto L3
 L2:
 iload 0
 iload 1
 iadd
 invokestatic Output/print(I)V
 goto L0
 L3:
 ldc 0
 invokestatic Output/print(I)V
 L0:
 return
.end method
```

## Note

- Le variabili vengono allocate dinamicamente nella symbol table con un indirizzo intero progressivo
- Usare una variabile non inizializzata genera un errore: `Error! Uninitialized variable: x`
- Il file `Output.j` viene generato nella stessa cartella di esecuzione
- Per eseguire il bytecode: `jasmin Output.j` poi `java Output`

## Struttura del codice

```
Translator
├── prog()       # punto di ingresso, genera label finale
├── stat()       # traduce singolo statement con label per il flusso
├── statlist()   # lista di statement
├── statlistp()  # continuazione lista
├── b_expr()     # traduce espressione booleana con salti condizionali
├── expr()       # traduce espressione aritmetica
├── exprp()      # emette iadd/isub
├── term()       # traduce termine
├── termp()      # emette imul/idiv
├── fact()       # emette ldc/iload o ricorre per parentesi
├── move()       # avanza al prossimo token
├── match()      # verifica e consuma un token
├── error()      # segnala errore sintattico
└── main()       # legge il file e avvia la traduzione

SymbolTable
├── insert(name, addr)     # inserisce una variabile
└── lookupAddress(name)    # restituisce l'indirizzo (-1 se non trovata)
```
