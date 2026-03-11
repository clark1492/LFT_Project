# Esercizio 2.1 — Analizzatore Lessicale (Lexer)

## Descrizione

Implementazione di un **analizzatore lessicale (lexer)**, prima fase di un compilatore. Il lexer legge un file sorgente carattere per carattere e lo trasforma in una sequenza di **token**, ovvero le unità atomiche del linguaggio.

## Token riconosciuti

| Categoria         | Simboli / Parole                          | Tag         |
|-------------------|-------------------------------------------|-------------|
| Parole chiave     | `if`, `then`, `else`, `for`, `do`         | 259–263     |
| I/O               | `read`, `print`                           | 264–265     |
| Operatori logici  | `&&`, `\|\|`, `!`                         | AND, OR     |
| Operatori relaz.  | `<`, `>`, `<=`, `>=`, `==`, `<>`         | RELOP (258) |
| Assegnamento      | `=`                                       | `=`         |
| Operatori aritm.  | `+`, `-`, `*`, `/`                        | ASCII       |
| Parentesi         | `(`, `)`                                  | ASCII       |
| Punto e virgola   | `;`                                       | ASCII       |
| Identificatori    | sequenze di lettere/cifre (inizia lettera)| ID (257)    |
| Numeri interi     | sequenze di cifre                         | NUM (256)   |
| Whitespace        | spazi, tab, newline                       | ignorati    |

## Struttura dei file

```
Esercizio 2.1/
├── Lexer.java       # analizzatore lessicale, contiene il main
├── Token.java       # classe base per i token
├── Word.java        # token per parole chiave e operatori composti
├── NumberTok.java   # token per i numeri interi
├── Tag.java         # costanti numeriche per i tipi di token
└── prova.txt        # file di input di esempio
```

## Compilazione ed esecuzione

Dalla cartella `Esercizio 2.1/`:

```bash
# Compilazione
mkdir -p classes
javac -d classes *.java

# Esecuzione
java -cp classes Lexer input.txt
```

## Esempio di output

Dato un file con contenuto:
```
if x == 3 then x = x + 1
```

Il lexer produce:
```
Scan: <259, if>
Scan: <257, x>
Scan: <258, ==>
Scan: <256, 3>
Scan: <260, then>
Scan: <257, x>
Scan: =
Scan: <257, x>
Scan: +
Scan: <256, 1>
Scan: <-1, EOF>
```

## Gestione degli errori

Il lexer segnala su `stderr` i caratteri non riconosciuti o le sequenze non valide, ad esempio:
```
Erroneous character after & : x   // '&' non seguito da '&'
Erroneous character: @            // carattere non nell'alfabeto
```

## Struttura del codice

```
Lexer
├── readch()          # legge il prossimo carattere dal BufferedReader
├── lexical_scan()    # logica principale — restituisce il prossimo token
└── main()            # legge il file e stampa i token in sequenza
```
