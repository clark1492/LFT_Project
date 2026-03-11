# Esercizio 2.2 — Lexer con identificatori estesi (underscore)

## Descrizione

Estensione dell'Esercizio 2.1. Il lexer ora supporta **identificatori con underscore** (`_`), validati internamente tramite un mini-DFA. La gestione dei numeri interi è stata inoltre riscritta per usare un approccio basato su automa.

## Differenze rispetto all'Esercizio 2.1

| Funzionalità                          | Es. 2.1 | Es. 2.2 |
|---------------------------------------|---------|---------|
| Identificatori con `_`                | ❌      | ✅      |
| Validazione ID tramite DFA interno    | ❌      | ✅      |
| Identificatori che iniziano con `_`   | ❌      | ✅      |
| Identificatori che terminano con `_`  | ❌      | ❌ (errore) |

## Regole per gli identificatori

Un identificatore è valido se:
- inizia con una **lettera** o un **underscore** (`_`)
- è seguito da lettere, cifre o underscore
- **non termina** con un underscore

Esempi validi: `x`, `_x`, `my_var`, `_hello1`
Esempi non validi: `x_`, `__`, `_` (solo underscore)

## Stati del DFA interno per gli identificatori

| Stato | Significato                                 | Accettante |
|-------|---------------------------------------------|------------|
| `0`   | Stato iniziale                              | ❌         |
| `1`   | Letto `_` (in attesa di lettera/cifra)      | ❌         |
| `2`   | Lettera letta — identificatore valido       | ✅         |
| `-1`  | Errore                                      | ❌         |

## Token riconosciuti

Gli stessi dell'Esercizio 2.1, con l'aggiunta del supporto a identificatori contenenti `_`:

| Categoria         | Simboli / Parole                           | Tag         |
|-------------------|--------------------------------------------|-------------|
| Parole chiave     | `if`, `then`, `else`, `for`, `do`          | 259–263     |
| I/O               | `read`, `print`                            | 264–265     |
| Operatori logici  | `&&`, `\|\|`, `!`                          | AND, OR     |
| Operatori relaz.  | `<`, `>`, `<=`, `>=`, `==`, `<>`          | RELOP (258) |
| Assegnamento      | `=`                                        | `=`         |
| Operatori aritm.  | `+`, `-`, `*`, `/`                         | ASCII       |
| Parentesi         | `(`, `)`                                   | ASCII       |
| Punto e virgola   | `;`                                        | ASCII       |
| Identificatori    | lettere/cifre/underscore (non finisce `_`) | ID (257)    |
| Numeri interi     | sequenze di cifre                          | NUM (256)   |

## Struttura dei file

```
Esercizio 2.2/
├── Lexer.java       # analizzatore lessicale con DFA interno per gli ID
├── Token.java       # classe base per i token
├── Word.java        # token per parole chiave e operatori composti
├── NumberTok.java   # token per i numeri interi
├── Tag.java         # costanti numeriche per i tipi di token
└── prova.txt        # file di input di esempio
```

## Compilazione ed esecuzione

Dalla cartella `Esercizio 2.2/`:

```bash
# Compilazione
mkdir -p build
javac -d build *.java

# Esecuzione
java -cp build Lexer input.txt
```

## Gestione degli errori

| Situazione                        | Messaggio di errore                      |
|-----------------------------------|------------------------------------------|
| Identificatore che finisce con `_`| `errore`                                 |
| Carattere non riconosciuto        | `Erroneous character: <char>`            |
| `&` non seguito da `&`            | `Erroneous character after & : <char>`   |
| `\|` non seguito da `\|`          | `Erroneous character after \| : <char>`  |

## Struttura del codice

```
Lexer
├── readch()          # legge il prossimo carattere
├── lexical_scan()    # logica principale con DFA interno per gli ID
└── main()            # legge il file e stampa i token in sequenza
```
