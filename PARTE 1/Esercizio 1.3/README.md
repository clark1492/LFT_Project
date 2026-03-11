# Esercizio 1.3 — DFA: numero di matricola + cognome (Turno 2 o Turno 3)

## Descrizione

Implementazione di un **automa a stati finiti deterministico (DFA)** che riconosce stringhe composte da un **numero di matricola** seguito immediatamente da un **cognome**, corrispondenti a studenti del **Turno 2** o del **Turno 3** del laboratorio di Linguaggi Formali e Traduttori.

## Regole dei turni

| Turno | Iniziale cognome | Matricola |
|-------|-----------------|-----------|
| 1     | `A-K`           | dispari   |
| **2** | **`A-K`**       | **pari**  |
| **3** | **`L-Z`**       | **dispari** |
| 4     | `L-Z`           | pari      |

Il DFA accetta **solo Turno 2 e Turno 3**, ovvero:
- matricola **pari** + cognome con iniziale **`A-K`**
- matricola **dispari** + cognome con iniziale **`L-Z`**

### Vincoli
- La matricola deve contenere **almeno una cifra**
- Il cognome deve contenere **almeno una lettera**
- Le due parti sono **concatenate senza separatori**

## Stati dell'automa

| Stato | Significato                                        | Accettante |
|-------|----------------------------------------------------|------------|
| `0`   | Stato iniziale                                     | ❌         |
| `1`   | Ultima cifra della matricola è **dispari**         | ❌         |
| `2`   | Ultima cifra della matricola è **pari**            | ❌         |
| `3`   | Cognome in corso (almeno una lettera letta)        | ✅         |
| `-1`  | Errore / combinazione non valida                   | ❌         |

## Diagramma delle transizioni

```
              pari                 dispari
      ┌──────────────[q0]───────────────┐
      ▼        pari       dispari       ▼
    (q2) ⟷────────────────────────── (q1)
      │                                 │
    A-K (turno 2)           L-Z (turno 3)
      └───────────► (q3) ◄─────────────┘
                     ⟲ a-z / A-Z
```

In forma tabellare:

| Da \ Input | pari (`0,2,4,6,8`) | dispari (`1,3,5,7,9`) | `A-K` | `L-Z` | `a-z` | altro |
|------------|--------------------|-----------------------|-------|-------|-------|-------|
| `q0`       | `q2`               | `q1`                  | `-1`  | `-1`  | `-1`  | `-1`  |
| `q1`       | `q2`               | `q1`                  | `-1`  | `q3`  | `-1`  | `-1`  |
| `q2`       | `q2`               | `q1`                  | `q3`  | `-1`  | `-1`  | `-1`  |
| `q3`       | `-1`               | `-1`                  | `q3`  | `q3`  | `q3`  | `-1`  |

## Compilazione ed esecuzione

```bash
javac "PARTE 1/Esercizio 1.3/Esercizio13.java"
java -cp "PARTE 1/Esercizio 1.3" Esercizio13 <stringa>
```

> ⚠️ Il programma accetta un **singolo argomento**. Se si passano più argomenti vengono concatenati con spazi, e lo spazio è un carattere non valido.

## Esempi

```bash
java Esercizio13 123456Bianchi   # → OK   (matricola pari, cognome A-K → Turno 2)
java Esercizio13 654321Rossi     # → OK   (matricola dispari, cognome L-Z → Turno 3)
java Esercizio13 2Bianchi        # → OK   (matricola pari, cognome A-K → Turno 2)
java Esercizio13 122B            # → OK   (matricola pari, cognome A-K → Turno 2)
java Esercizio13 654321Bianchi   # → NOPE (matricola dispari, cognome A-K → Turno 1)
java Esercizio13 123456Rossi     # → NOPE (matricola pari, cognome L-Z → Turno 4)
java Esercizio13 654322          # → NOPE (manca il cognome)
java Esercizio13 Rossi           # → NOPE (manca la matricola)
```

## Struttura del codice

```
Esercizio 1.3/
└── Esercizio13.java
      ├── scan(String s) → boolean   # logica del DFA
      └── main(String[] args)        # entry point
```
