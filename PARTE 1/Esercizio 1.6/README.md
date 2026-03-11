# Esercizio 1.6 — DFA: numeri binari multipli di 3

## Descrizione

Implementazione di un **automa a stati finiti deterministico (DFA)** che riconosce stringhe binarie (su alfabeto `{0, 1}`) il cui valore intero è **multiplo di 3**.

## Idea chiave

Leggendo un numero binario da sinistra a destra, ad ogni passo il valore corrente `v` si aggiorna come:
- leggo `0` → `v = 2v`
- leggo `1` → `v = 2v + 1`

Il **resto della divisione per 3** segue le stesse regole:
- leggo `0` → `r = (2r) mod 3`
- leggo `1` → `r = (2r + 1) mod 3`

Si usano quindi stati per tracciare il resto, con la complicazione che la stringa `"0"` (che vale 0, multiplo di 3) deve essere distinta dalla stringa vuota. Per questo lo stato `0` è lo stato iniziale non accettante, mentre `1` rappresenta resto 0 dopo aver letto almeno un carattere.

## Stati dell'automa

| Stato | Significato                                      | Accettante |
|-------|--------------------------------------------------|------------|
| `0`   | Stato iniziale (nessun carattere letto)          | ❌         |
| `1`   | Letto `0` come primo carattere → valore = 0     | ✅         |
| `2`   | Dopo `0` iniziale, qualsiasi cifra → trappola    | ❌         |
| `3`   | Resto = 1 (numero letto fin qui ≡ 1 mod 3)      | ❌         |
| `4`   | Resto = 0 (numero letto fin qui ≡ 0 mod 3)      | ✅         |
| `5`   | Resto = 2 (numero letto fin qui ≡ 2 mod 3)      | ❌         |
| `-1`  | Carattere non valido                             | ❌         |

> **Nota:** gli stati `1` e `2` gestiscono il caso speciale della stringa che inizia con `0`. Poiché un numero binario valido non ha zeri iniziali (tranne `"0"` stesso), dopo uno `0` iniziale qualsiasi altro carattere porta in uno stato trappola non accettante.

## Diagramma delle transizioni

```
              0             1
       [q0] ───► (q1)✓    [q0] ───► (q3)
        
  (q1) ──0,1──► (q2)✗⟲0,1     (trappola per "0..." con più cifre)

  (q3) ──0──► (q5)          (q3) ──1──► (q4)✓
  (q4) ──0──► (q4)✓⟲0      (q4) ──1──► (q3)
  (q5) ──0──► (q3)          (q5) ──1──► (q5)⟲1
```

In forma tabellare:

| Da \ Input | `0`  | `1`  |
|------------|------|------|
| `q0`       | `q1` | `q3` |
| `q1`       | `q2` | `q2` |
| `q2`       | `q2` | `q2` |
| `q3`       | `q5` | `q4` |
| `q4`       | `q4` | `q3` |
| `q5`       | `q3` | `q5` |

## Verifica della logica (resto mod 3)

| Stringa | Valore | Resto mod 3 | Risultato |
|---------|--------|-------------|-----------|
| `0`     | 0      | 0           | ✅ OK     |
| `11`    | 3      | 0           | ✅ OK     |
| `110`   | 6      | 0           | ✅ OK     |
| `1001`  | 9      | 0           | ✅ OK     |
| `10`    | 2      | 2           | ❌ NOPE   |
| `111`   | 7      | 1           | ❌ NOPE   |
| `01`    | —      | —           | ❌ NOPE   |

## Compilazione ed esecuzione

```bash
javac "PARTE 1/Esercizio 1.6/Esercizio16.java"
java -cp "PARTE 1/Esercizio 1.6" Esercizio16 <stringa>
```

> ⚠️ Il programma accetta un **singolo argomento**. Se si passano più argomenti vengono concatenati con spazi, e lo spazio è un carattere non valido.

## Esempi

```bash
java Esercizio16 0       # → OK   (0 è multiplo di 3)
java Esercizio16 11      # → OK   (3)
java Esercizio16 110     # → OK   (6)
java Esercizio16 1001    # → OK   (9)
java Esercizio16 10      # → NOPE (2)
java Esercizio16 111     # → NOPE (7)
java Esercizio16 01      # → NOPE (zero iniziale non valido)
```

## Struttura del codice

```
Esercizio 1.6/
└── Esercizio16.java
      ├── scan(String s) → boolean   # logica del DFA
      └── main(String[] args)        # entry point
```
