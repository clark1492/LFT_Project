# Esercizio 4.1 — Valutatore di espressioni aritmetiche

## Descrizione

Estensione dell'Esercizio 3.1. Oltre al **parser** che verifica la correttezza sintattica, viene introdotto il **Valutatore**: un secondo componente che percorre la grammatica nello stesso modo del parser ma, invece di limitarsi a riconoscere l'input, **calcola il valore numerico** dell'espressione usando attributi sintetizzati.

## Differenze rispetto all'Esercizio 3.1

| Funzionalità                        | Es. 3.1   | Es. 4.1     |
|-------------------------------------|-----------|-------------|
| Verifica sintattica                 | ✅ Parser | ✅ Parser   |
| Calcolo del valore dell'espressione | ❌        | ✅ Valutatore |
| Attributi sintetizzati              | ❌        | ✅          |

## Grammatica e attributi sintetizzati

```
start  → expr EOF          { print(expr.val) }
expr   → term exprp        { exprp.i = term.val;  expr.val = exprp.s }
exprp  → + term exprp1     { exprp1.i = exprp.i + term.val; exprp.s = exprp1.s }
       | - term exprp1     { exprp1.i = exprp.i - term.val; exprp.s = exprp1.s }
       | ε                 { exprp.s = exprp.i }
term   → fact termp        { termp.i = fact.val;  term.val = termp.s }
termp  → * fact termp1     { termp1.i = termp.i * fact.val; termp.s = termp1.s }
       | / fact termp1     { termp1.i = termp.i / fact.val; termp.s = termp1.s }
       | ε                 { termp.s = termp.i }
fact   → ( expr )          { fact.val = expr.val }
       | NUM               { fact.val = NUM.val }
```

Ogni metodo restituisce un `int` che rappresenta il valore calcolato della sottoespressione.

## Struttura dei file

```
Esercizio 4.1/
├── Parser.java      # parser sintattico (da Es. 3.1)
├── Valutatore.java  # valutatore con attributi sintetizzati
├── Lexer.java       # analizzatore lessicale
├── Token.java       # classe base per i token
├── Word.java        # token per parole chiave e operatori
├── NumberTok.java   # token per i numeri interi
├── Tag.java         # costanti per i tipi di token
└── input.txt        # file di input di esempio
```

## Compilazione ed esecuzione

Dalla cartella `Esercizio 4.1/`:

```bash
# Compilazione
mkdir -p build
javac -d build *.java

# Esegui il Parser (solo verifica sintattica)
java -cp build Parser input.txt

# Esegui il Valutatore (calcola il risultato)
java -cp build Valutatore input.txt
```

## Esempi

Input (`input.txt`):
```
(3 + 4) * 2
```

Output del Parser:
```
token = <40>
token = <256, 3>
...
Input OK
```

Output del Valutatore:
```
token = <40>
token = <256, 3>
...
14
```

## Note

- Il file di input deve contenere **una sola espressione** per file
- L'alfabeto è limitato a numeri interi, `+`, `-`, `*`, `/` e parentesi tonde
- La divisione è intera (es. `7 / 2 = 3`)

## Struttura del codice

```
Valutatore
├── start()        # punto di ingresso, stampa il risultato finale
├── expr()  → int  # valuta somme/sottrazioni
├── exprp() → int  # continuazione di expr (riceve valore accumulato)
├── term()  → int  # valuta moltiplicazioni/divisioni
├── termp() → int  # continuazione di term (riceve valore accumulato)
├── fact()  → int  # valuta numero o sottoespressione tra parentesi
├── move()         # avanza al prossimo token
├── match()        # verifica e consuma un token
├── error()        # segnala errore sintattico
└── main()         # legge il file e avvia la valutazione
```
