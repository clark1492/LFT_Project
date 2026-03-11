# Esercizio 1.7 — DFA: commenti `/* ... */`

## Descrizione

Implementazione di un **automa a stati finiti deterministico (DFA)** sull'alfabeto `{/, *, a}` che riconosce stringhe che rappresentano **commenti in stile C**, delimitati da `/*` all'inizio e `*/` alla fine, con una sola occorrenza della sequenza di chiusura `*/`.

## Linguaggio riconosciuto

Una stringa è valida se:
- inizia esattamente con `/*`
- finisce esattamente con `*/`
- contiene **una sola occorrenza** della sequenza `*/` (quella finale)
- l'`*` di chiusura **non è in comune** con quello di apertura (quindi `/**/` è valido ma `/*/` no)

## Stati dell'automa

| Stato | Significato                                      | Accettante |
|-------|--------------------------------------------------|------------|
| `0`   | Stato iniziale, attende `/`                      | ❌         |
| `1`   | Letto `/`, attende `*` di apertura               | ❌         |
| `2`   | Dentro il commento, nessun `*` in coda           | ❌         |
| `3`   | Dentro il commento, uno o più `*` in coda        | ❌         |
| `4`   | Letto `*/` — commento chiuso                     | ✅         |
| `-1`  | Errore                                           | ❌         |

## Diagramma delle transizioni

```
          /           *          a,/        *
  [q0] ──────► [q1] ──────► (q2) ⟲─────── (q3) ⟲*
                              │              │  │
                              └──── * ──────►│  │
                                             │  └─── / ───► (q4)✓ ──── qualsiasi ───► -1
                                             └─── a ───► (q2)
```

In forma tabellare:

| Da \ Input | `/`  | `*`  | `a`  |
|------------|------|------|------|
| `q0`       | `q1` | `-1` | `-1` |
| `q1`       | `-1` | `q2` | `-1` |
| `q2`       | `q2` | `q3` | `q2` |
| `q3`       | `q4` | `q3` | `q2` |
| `q4`       | `-1` | `-1` | `-1` |

## Compilazione ed esecuzione

```bash
javac "PARTE 1/Esercizio 1.7/Esercizio17.java"
java -cp "PARTE 1/Esercizio 1.7" Esercizio17 <stringa>
```

> ⚠️ Il programma accetta un **singolo argomento**. Se si passano più argomenti vengono concatenati con spazi, e lo spazio è un carattere non valido.

## Esempi

```bash
java Esercizio17 "/**/"          # → OK   (commento vuoto)
java Esercizio17 "/****/"        # → OK   (solo asterischi interni)
java Esercizio17 "/*a*a*/"       # → OK   (contenuto misto)
java Esercizio17 "/*a/*/"        # → OK   (slash interno permesso)
java Esercizio17 "/**a///a/a**/" # → OK
java Esercizio17 "/*/*/"         # → OK   (slash dopo apertura è contenuto)
java Esercizio17 "/*/"           # → NOPE (* di apertura usato anche per chiusura)
java Esercizio17 "/**/***/"`     # → NOPE (doppia occorrenza di */)
```

## Struttura del codice

```
Esercizio 1.7/
└── Esercizio17.java
      ├── scan(String s) → boolean   # logica del DFA
      └── main(String[] args)        # entry point
```
