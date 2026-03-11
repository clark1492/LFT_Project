# Esercizio 1.1 — DFA: stringhe binarie senza `000`

## Descrizione

Implementazione di un **automa a stati finiti deterministico (DFA)** che riconosce stringhe binarie sull'alfabeto `{0, 1}` che **non contengono la sottostringa `000`**.

## Linguaggio riconosciuto

$$L = \{ w \in \{0,1\}^* \mid w \text{ non contiene } 000 \text{ come sottostringa} \}$$

## Stati dell'automa

| Stato | Significato                        | Accettante |
|-------|------------------------------------|------------|
| `0`   | Nessuno `0` consecutivo in coda    | ✅         |
| `1`   | Un `0` consecutivo in coda         | ✅         |
| `2`   | Due `0` consecutivi in coda        | ✅         |
| `3`   | Trovati tre `0` consecutivi (trap) | ❌         |
| `-1`  | Carattere non valido               | ❌         |

### Diagramma delle transizioni

```
        1           1           1
   ┌───────┐   ┌───────┐   ┌───────┐
   │       ▼   │       ▼   │       ▼
→(q0) ──0──► (q1) ──0──► (q2) ──0──► [q3]
   ▲           ▲           ▲
   └─────1─────┘           │
   └──────────────1────────┘
```

## Compilazione ed esecuzione

```bash
javac "PARTE 1/Esercizio 1.1/Esercizio11.java"
java -cp "PARTE 1/Esercizio 1.1" Esercizio11 <stringa>
```

> ⚠️ Il programma accetta un **singolo argomento**. Se si passano più argomenti vengono concatenati con spazi, e lo spazio è un carattere non valido.

## Esempi

```bash
java Esercizio11 1010011   # → OK
java Esercizio11 110       # → OK
java Esercizio11 1001      # → OK  (solo due 0 consecutivi)
java Esercizio11 000       # → NOPE
java Esercizio11 10001     # → NOPE (contiene 000)
java Esercizio11 1234      # → NOPE (caratteri non validi)
```

## Struttura del codice

```
Esercizio 1.1/
└── Esercizio11.java
      ├── scan(String s) → boolean   # logica del DFA
      └── main(String[] args)        # entry point
```
