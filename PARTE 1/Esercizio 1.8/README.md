# Esercizio 1.8 — DFA: sequenze con commenti `/* ... */` multipli

## Descrizione

Estensione dell'Esercizio 1.7. Il DFA riconosce stringhe sull'alfabeto `{/, *, a}` che possono contenere **zero o più commenti** in stile C (`/* ... */`) immersi in sequenze arbitrarie di simboli. L'unico vincolo è che ogni apertura `/*` deve essere seguita da una chiusura `*/`.

## Linguaggio riconosciuto

Una stringa è valida se:
- può contenere qualsiasi sequenza di `a`, `/`, `*` **fuori dai commenti**
- ogni occorrenza di `/*` deve essere seguita (anche non immediatamente) da `*/`
- i commenti non si possono annidare: dentro un commento, `/*` è trattato come contenuto
- è valida anche la stringa **senza nessun commento** (zero occorrenze di `/*`)
- **non** è valida una stringa con un `/*` senza la relativa chiusura `*/`

## Stati dell'automa

| Stato | Significato                                               | Accettante |
|-------|-----------------------------------------------------------|------------|
| `0`   | Fuori da un commento (stato "normale")                    | ✅         |
| `1`   | Letto `/` fuori dal commento (possibile apertura)         | ✅         |
| `2`   | Dentro il commento, nessun `*` in coda                    | ❌         |
| `3`   | Dentro il commento, uno o più `*` in coda                 | ❌         |
| `-1`  | Errore                                                    | ❌         |

Gli stati `0` e `1` sono entrambi accettanti: in `1` è stato letto un `/` isolato che non ha ancora aperto un commento, il che è comunque valido.

## Differenze rispetto all'Esercizio 1.7

| Aspetto                         | Es. 1.7 | Es. 1.8 |
|---------------------------------|---------|---------|
| Caratteri prima/dopo commento   | ❌      | ✅      |
| Commenti multipli               | ❌      | ✅      |
| Stringa senza commenti          | ❌      | ✅      |
| Stato accettante dopo `*/`      | `q4`    | `q0`    |

La differenza chiave è che in Es. 1.8 dopo la chiusura `*/` si torna allo stato `q0` (fuori dal commento), permettendo ulteriore contenuto o altri commenti.

## Diagramma delle transizioni

```
         a,*          /           *           a,/
  (q0)⟲──────  (q0) ──────► (q1) ──────► (q2) ⟲──────  (q3)⟲*
                ▲             │  \           │              │
                │           a │   / ─► (q1) └──── * ──────►│
                │             ▼                             │
                └────────────(q3) ─────── / ──────────────►(q0)
                               a ──────────────────────────►(q2)
```

In forma tabellare:

| Da \ Input | `a`  | `/`  | `*`  |
|------------|------|------|------|
| `q0`       | `q0` | `q1` | `q0` |
| `q1`       | `q0` | `q1` | `q2` |
| `q2`       | `q2` | `q2` | `q3` |
| `q3`       | `q2` | `q0` | `q3` |

## Compilazione ed esecuzione

```bash
javac "PARTE 1/Esercizio 1.8/Esercizio18.java"
java -cp "PARTE 1/Esercizio 1.8" Esercizio18 <stringa>
```

> ⚠️ Il programma accetta un **singolo argomento**. Se si passano più argomenti vengono concatenati con spazi, e lo spazio è un carattere non valido.

## Esempi

```bash
java Esercizio18 "aaa/****/aa"     # → OK   (commento con contenuto + testo fuori)
java Esercizio18 "aa/*a*a*/"       # → OK
java Esercizio18 "aaaa"            # → OK   (nessun commento)
java Esercizio18 "/****/"          # → OK   (solo commento)
java Esercizio18 "*/a"             # → OK   (* e / isolati non aprono commento)
java Esercizio18 "a/**/***a"       # → OK   (commento vuoto + * fuori)
java Esercizio18 "a/**/***/a"      # → OK   (commenti multipli)
java Esercizio18 "a/**/aa/***/a"   # → OK   (due commenti separati da testo)
java Esercizio18 "aaa/*/aa"        # → NOPE (/* aperto senza chiusura valida)
java Esercizio18 "aa/*aa"          # → NOPE (/* aperto mai chiuso)
```

## Struttura del codice

```
Esercizio 1.8/
└── Esercizio18.java
      ├── scan(String s) → boolean   # logica del DFA
      └── main(String[] args)        # entry point
```
