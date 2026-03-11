# Esercizio 3.1 — Parser per espressioni aritmetiche

## Descrizione

Implementazione di un **parser a discesa ricorsiva** che riconosce espressioni aritmetiche composte da numeri interi, operatori `+`, `-`, `*`, `/` e parentesi tonde. Il parser si appoggia al lexer dell'Esercizio 2.1 per la tokenizzazione dell'input.

## Grammatica riconosciuta

```
start  → expr EOF
expr   → term exprp
exprp  → + term exprp
       | - term exprp
       | ε
term   → fact termp
termp  → * fact termp
       | / fact termp
       | ε
fact   → ( expr )
       | NUM
```

La grammatica è **LL(1)**: ad ogni passo il parser decide quale produzione applicare guardando solo il token corrente (`look`).

## Struttura del parser

| Metodo     | Grammatica       | Descrizione                                  |
|------------|------------------|----------------------------------------------|
| `start()`  | `start → expr EOF` | punto di ingresso, verifica EOF finale     |
| `expr()`   | `expr → term exprp` | gestisce somme e sottrazioni              |
| `exprp()`  | `exprp → ...`    | parte destra di `expr` (ricorsiva)           |
| `term()`   | `term → fact termp` | gestisce moltiplicazioni e divisioni      |
| `termp()`  | `termp → ...`    | parte destra di `term` (ricorsiva)           |
| `fact()`   | `fact → (expr) \| NUM` | gestisce numeri e sottoespressioni     |

## Metodi di supporto

| Metodo    | Descrizione                                                    |
|-----------|----------------------------------------------------------------|
| `move()`  | avanza al prossimo token chiamando il lexer                    |
| `match(t)`| verifica che il token corrente sia `t`, poi avanza             |
| `error(s)`| lancia un'eccezione con il numero di riga e il messaggio       |

## Struttura dei file

```
Esercizio 3.1/
├── Parser.java      # parser a discesa ricorsiva, contiene il main
├── Lexer.java       # analizzatore lessicale (da Esercizio 2.1)
├── Token.java       # classe base per i token
├── Word.java        # token per parole chiave e operatori
├── NumberTok.java   # token per i numeri interi
├── Tag.java         # costanti per i tipi di token
└── prova.txt        # file di input di esempio
```

## Compilazione ed esecuzione

Dalla cartella `Esercizio 3.1/`:

```bash
# Compilazione
mkdir -p build
javac -d build *.java

# Esecuzione
java -cp build Parser
```


## Esempi

Input validi:
```
1 + 2
(3 + 4) * 5
10 / 2 - 3 * (1 + 1)
```

Input non validi:
```
1 +          ← espressione incompleta
(1 + 2       ← parentesi non chiusa
1 2          ← operatore mancante
```

Output per input valido:
```
token = <256, 1>
token = +
token = <256, 2>
token = <-1>
Input OK
```

Output per input non valido:
```
token = <256, 1>
token = +
token = <-1>
Exception: near line 1: syntax error
```

## Struttura del codice

```
Parser
├── start()    # punto di ingresso
├── expr()     # somme/sottrazioni
├── exprp()    # continuazione di expr (ricorsiva)
├── term()     # moltiplicazioni/divisioni
├── termp()    # continuazione di term (ricorsiva)
├── fact()     # numero o sottoespressione tra parentesi
├── move()     # avanza al prossimo token
├── match()    # verifica e consuma un token
├── error()    # segnala un errore sintattico
└── main()     # legge Prova.txt e avvia il parsing
```
