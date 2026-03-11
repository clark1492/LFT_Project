# Esercizio 1.2 — DFA: identificatori validi

## Descrizione

Implementazione di un **automa a stati finiti deterministico (DFA)** che riconosce **identificatori validi**, ovvero stringhe che iniziano con una lettera (o una sequenza di `_`) e sono seguite da lettere, cifre o underscore.

## Linguaggio riconosciuto

Un identificatore è valido se:
- inizia con una **lettera** (`a-z`, `A-Z`), eventualmente preceduta da uno o più `_`
- è seguito da qualsiasi combinazione di **lettere**, **cifre** (`0-9`) e **underscore** (`_`)
- **non inizia direttamente con una cifra**

## Stati dell'automa

| Stato | Significato                                      | Accettante |
|-------|--------------------------------------------------|------------|
| `0`   | Stato iniziale                                   | ❌         |
| `1`   | Cifra in prima posizione (trappola)              | ❌         |
| `2`   | Lettera letta — identificatore valido in corso   | ✅         |
| `3`   | Underscore in coda (non ancora valido)           | ❌         |
| `-1`  | Carattere non valido                             | ❌         |

### Diagramma delle transizioni

```
                        lettera
              ┌─────────────────────────────┐
              │      lettera/cifra/_        │
  cifra  ┌──[q1]◄──── cifra ────[q0]       ▼
(trappola)│          lettera ────────────►(q2)⟲ lettera/cifra/_
          │          _ ──────────────────►(q3)⟲ _
          │                               ▲
          └───────────────────────────────┘
                        lettera/cifra
```

In forma tabellare:

| Da \ Input | lettera | cifra | `_`  | altro |
|------------|---------|-------|------|-------|
| `q0`       | `q2`    | `q1`  | `q3` | `-1`  |
| `q1`       | `-1`    | `-1`  | `-1` | `-1`  |
| `q2`       | `q2`    | `q2`  | `q2` | `-1`  |
| `q3`       | `q2`    | `q2`  | `q3` | `-1`  |

## Compilazione ed esecuzione

```bash
javac "PARTE 1/Esercizio 1.2/Esercizio12.java"
java -cp "PARTE 1/Esercizio 1.2" Esercizio12 <stringa>
```

> ⚠️ Il programma accetta un **singolo argomento**. Se si passano più argomenti vengono concatenati con spazi, e lo spazio è un carattere non valido.

## Esempi

```bash
java Esercizio12 hello         # → OK
java Esercizio12 _hello        # → OK
java Esercizio12 __a1          # → OK
java Esercizio12 var_1         # → OK
java Esercizio12 1hello        # → NOPE  (inizia con cifra)
java Esercizio12 ___           # → NOPE  (solo underscore, nessuna lettera)
java Esercizio12 "hello world" # → NOPE  (spazio non valido)
```

## Struttura del codice

```
Esercizio 1.2/
└── Esercizio12.java
      ├── scan(String s) → boolean   # logica del DFA
      └── main(String[] args)        # entry point
```
