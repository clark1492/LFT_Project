# Esercizio 2.3 — Lexer con commenti `/* */` e `//`

## Descrizione

Estensione dell'Esercizio 2.2. Il lexer ora riconosce e **ignora i commenti** in stile C, sia su singola riga (`//`) che su più righe (`/* ... */`). Aggiunge inoltre una gestione più robusta dei numeri, che ora impedisce sequenze come `123abc`.

## Differenze rispetto all'Esercizio 2.2

| Funzionalità                            | Es. 2.2 | Es. 2.3 |
|-----------------------------------------|---------|---------|
| Commenti `// ...` (riga singola)        | ❌      | ✅      |
| Commenti `/* ... */` (multi-riga)       | ❌      | ✅      |
| Numero seguito da lettera → errore      | ❌      | ✅      |
| Operatori `<`, `>`, `=` senza spazio   | ❌      | ✅      |

## Gestione dei commenti

### Commento su singola riga `//`
Tutto ciò che segue `//` fino al fine riga viene ignorato. Il lexer riprende dalla riga successiva.

### Commento multi-riga `/* ... */`
Tutto ciò che si trova tra `/*` e `*/` viene ignorato, inclusi i newline. Se il commento non viene chiuso prima di EOF, viene segnalato un errore.

```java
// questo è un commento su una riga
/* questo è un
   commento su più righe */
```

## Token riconosciuti

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
| Numeri interi     | sequenze di sole cifre                     | NUM (256)   |
| Commenti          | `// ...` e `/* ... */`                     | ignorati    |

## Struttura dei file

```
Esercizio 2.3/
├── Lexer.java       # lexer con gestione commenti
├── Token.java       # classe base per i token
├── Word.java        # token per parole chiave e operatori composti
├── NumberTok.java   # token per i numeri interi
├── Tag.java         # costanti numeriche per i tipi di token
└── prova.txt        # file di input di esempio
```

## Compilazione ed esecuzione

Dalla cartella `Esercizio 2.3/`:

```bash
# Compilazione
mkdir -p build
javac -d build *.java

# Esecuzione
java -cp build Lexer input.txt
```

## Esempio di input

```java
/* calcola la somma */
if (x == 3) then  // controlla x
    x = x + 1;
```

Output atteso:
```
Scan: <259, if>
Scan: <40>
Scan: <257, x>
Scan: <258, ==>
Scan: <256, 3>
Scan: <41>
Scan: <260, then>
Scan: <257, x>
Scan: =
Scan: <257, x>
Scan: +
Scan: <256, 1>
Scan: <59>
Scan: <-1>
```

## Gestione degli errori

| Situazione                          | Messaggio di errore                                              |
|-------------------------------------|------------------------------------------------------------------|
| Commento `/*` non chiuso            | `Erroneous note not ended`                                       |
| Numero seguito da lettera/underscore| `Erroneus character: the number can't be at start of expression` |
| Identificatore che finisce con `_`  | `errore`                                                         |
| Carattere non riconosciuto          | `Erroneous character: <char>`                                    |
| `&` non seguito da `&`              | `Erroneous character after & : <char>`                           |

## Struttura del codice

```
Lexer
├── readch()          # legge il prossimo carattere
├── lexical_scan()    # logica principale, con gestione ricorsiva dei commenti
└── main()            # legge Prova.txt e stampa i token in sequenza
```
