# Esercizio 1.5 — DFA: cognome + matricola (Turno 2 o Turno 3)

## Descrizione

Variante dell'Esercizio 1.3 in cui **il cognome precede il numero di matricola**. Il DFA riconosce stringhe composte da un **cognome** seguito immediatamente da un **numero di matricola**, corrispondenti a studenti del **Turno 2** o del **Turno 3** del laboratorio di Linguaggi Formali e Traduttori.

## Regole dei turni

| Turno | Iniziale cognome | Matricola   |
|-------|-----------------|-------------|
| 1     | `A-K`           | dispari     |
| **2** | **`A-K`**       | **pari**    |
| **3** | **`L-Z`**       | **dispari** |
| 4     | `L-Z`           | pari        |

Il DFA accetta **solo Turno 2 e Turno 3**:
- cognome con iniziale **`A-K`** + matricola **pari**
- cognome con iniziale **`L-Z`** + matricola **dispari**

## Stati dell'automa

| Stato | Significato                                                        | Accettante |
|-------|--------------------------------------------------------------------|------------|
| `0`   | Stato iniziale                                                     | ❌         |
| `1`   | Cognome iniziato con `L-Z` (turno 3 candidato)                    | ❌         |
| `2`   | Cognome iniziato con `A-K` (turno 2 candidato)                    | ❌         |
| `3`   | Matricola in corso, ultima cifra **dispari** (dopo cognome `L-Z`) | ✅         |
| `4`   | Matricola in corso, ultima cifra **pari** (dopo cognome `A-K`)    | ✅         |
| `5`   | Matricola in corso, ultima cifra **pari** (dopo cognome `L-Z`)    | ❌         |
| `6`   | Matricola in corso, ultima cifra **dispari** (dopo cognome `A-K`) | ❌         |
| `-1`  | Errore / combinazione non valida                                   | ❌         |

Gli stati `3` e `4` sono accettanti perché rappresentano rispettivamente Turno 3 (L-Z + dispari) e Turno 2 (A-K + pari).

## Diagramma delle transizioni

```
         A-K              L-Z
  [q0] ──────► (q2) ⟲a-z    (q1) ⟲a-z ◄────── [q0]
                │                 │
           pari │            dispari
                ▼                 ▼
              (q4)✓            (q3)✓
          ⟲pari  │dispari  pari│  ⟲dispari
                 ▼             ▼
               (q6)✗         (q5)✗
          ⟲dispari│pari  dispari│⟲pari
                  └──────┬──────┘
                      (ciclo)
```

In forma tabellare:

| Da \ Input | `A-K` | `L-Z` | `a-z` | pari  | dispari | altro |
|------------|-------|-------|-------|-------|---------|-------|
| `q0`       | `q2`  | `q1`  | `-1`  | `-1`  | `-1`    | `-1`  |
| `q1`       | `-1`  | `-1`  | `q1`  | `q5`  | `q3`    | `-1`  |
| `q2`       | `-1`  | `-1`  | `q2`  | `q4`  | `q6`    | `-1`  |
| `q3`       | `-1`  | `-1`  | `-1`  | `q5`  | `q3`    | `-1`  |
| `q4`       | `-1`  | `-1`  | `-1`  | `q4`  | `q6`    | `-1`  |
| `q5`       | `-1`  | `-1`  | `-1`  | `q5`  | `q3`    | `-1`  |
| `q6`       | `-1`  | `-1`  | `-1`  | `q4`  | `q6`    | `-1`  |

## Compilazione ed esecuzione

```bash
javac "PARTE 1/Esercizio 1.5/Esercizio15.java"
java -cp "PARTE 1/Esercizio 1.5" Esercizio15 <stringa>
```

> ⚠️ Il programma accetta un **singolo argomento**. Se si passano più argomenti vengono concatenati con spazi, e lo spazio è un carattere non valido.

## Esempi

```bash
java Esercizio15 Bianchi123456   # → OK   (A-K + pari → Turno 2)
java Esercizio15 Rossi654321     # → OK   (L-Z + dispari → Turno 3)
java Esercizio15 B2              # → OK   (A-K + pari → Turno 2)
java Esercizio15 Rossi654322     # → NOPE (L-Z + pari → Turno 4)
java Esercizio15 Bianchi654321   # → NOPE (A-K + dispari → Turno 1)
java Esercizio15 123456Bianchi   # → NOPE (matricola prima del cognome)
java Esercizio15 Bianchi         # → NOPE (manca la matricola)
```

## Differenze rispetto all'Esercizio 1.3

| Aspetto              | Es. 1.3              | Es. 1.5              |
|----------------------|----------------------|----------------------|
| Ordine               | matricola + cognome  | cognome + matricola  |
| Stato accettante     | `q3`                 | `q3`, `q4`           |
| Numero di stati      | 4 (+trap)            | 7 (+trap)            |

## Struttura del codice

```
Esercizio 1.5/
└── Esercizio15.java
      ├── scan(String s) → boolean   # logica del DFA
      └── main(String[] args)        # entry point
```
