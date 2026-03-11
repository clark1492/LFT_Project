# Esercizio 3.2 — Parser per statement e programmi

## Descrizione

Estensione dell'Esercizio 3.1. Il parser ora riconosce non solo espressioni aritmetiche ma un **linguaggio imperativo completo**, con statement di assegnamento, I/O, condizionali, cicli e blocchi. Si appoggia al lexer dell'Esercizio 2.1.

## Differenze rispetto all'Esercizio 3.1

| Funzionalità                        | Es. 3.1 | Es. 3.2 |
|-------------------------------------|---------|---------|
| Espressioni aritmetiche             | ✅      | ✅      |
| Identificatori nelle espressioni    | ❌      | ✅      |
| Assegnamento (`x = expr`)           | ❌      | ✅      |
| `print(expr)`                       | ❌      | ✅      |
| `read(id)`                          | ❌      | ✅      |
| `if bexpr then stat else stat`      | ❌      | ✅      |
| `for (id = expr; bexpr) do stat`    | ❌      | ✅      |
| Blocchi `begin ... end`             | ❌      | ✅      |
| Lista di statement separati da `;`  | ❌      | ✅      |
| Espressioni booleane (RELOP)        | ❌      | ✅      |

## Grammatica riconosciuta

```
prog      → statlist EOF
statlist  → stat statlistp
statlistp → ; stat statlistp | ε
stat      → id = expr
           | print ( expr )
           | read ( id )
           | if bexpr then stat [ else stat ]
           | for ( id = expr ; bexpr ) do stat
           | begin statlist end
bexpr     → expr RELOP expr
expr      → term exprp
exprp     → + term exprp | - term exprp | ε
term      → fact termp
termp     → * fact termp | / fact termp | ε
fact      → ( expr ) | NUM | ID
```

## Struttura del parser

| Metodo        | Descrizione                                          |
|---------------|------------------------------------------------------|
| `prog()`      | punto di ingresso, verifica EOF finale               |
| `statlist()`  | lista di statement                                   |
| `statlistp()` | continuazione della lista (separatore `;`)           |
| `stat()`      | singolo statement (assign, print, read, if, for, begin) |
| `bexpr()`     | espressione booleana con operatore relazionale       |
| `expr()`      | espressione aritmetica                               |
| `exprp()`     | continuazione di `expr`                              |
| `term()`      | termine aritmetico                                   |
| `termp()`     | continuazione di `term`                              |
| `fact()`      | fattore: numero, identificatore o sottoespressione   |

## Struttura dei file

```
Esercizio 3.2/
├── Parser.java      # parser a discesa ricorsiva, contiene il main
├── Lexer.java       # analizzatore lessicale
├── Token.java       # classe base per i token
├── Word.java        # token per parole chiave e operatori
├── NumberTok.java   # token per i numeri interi
├── Tag.java         # costanti per i tipi di token
└── prova.txt        # file di input di esempio
```

## Compilazione ed esecuzione

Dalla cartella `Esercizio 3.2/`:

```bash
# Compilazione
mkdir -p build
javac -d build *.java

# Esecuzione
java -cp build Parser prova.txt
```

## Esempi di input validi

```
x = 3
```
```
read(x); print(x + 1)
```
```
if x == 3 then x = x + 1 else x = 0
```
```
begin
  read(x);
  if x > 0 then print(x) else print(0)
end
```
```
for (i = 0; i < 10) do print(i)
```

## Gestione degli errori

In caso di errore sintattico il parser lancia un'eccezione con il numero di riga:
```
Exception: near line 2: syntax error in stat
```

## Struttura del codice

```
Parser
├── prog()       # punto di ingresso
├── statlist()   # lista di statement
├── statlistp()  # continuazione lista
├── stat()       # singolo statement
├── bexpr()      # espressione booleana
├── expr()       # espressione aritmetica
├── exprp()      # continuazione expr
├── term()       # termine
├── termp()      # continuazione term
├── fact()       # fattore
├── move()       # avanza al prossimo token
├── match()      # verifica e consuma un token
├── error()      # segnala errore sintattico
└── main()       # legge il file e avvia il parsing
```
