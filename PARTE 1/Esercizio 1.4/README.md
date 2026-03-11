# Esercizio 1.4 — DFA: matricola + cognome con spazi (Turno 2 o Turno 3)

## Descrizione

Estensione dell'Esercizio 1.3. Il DFA riconosce le stesse combinazioni di **matricola + cognome** per il **Turno 2** e il **Turno 3**, ma con le seguenti varianti:

- la stringa può essere **preceduta e/o seguita** da sequenze di spazi
- matricola e cognome possono essere **separati da uno o più spazi**
- sono accettati **cognomi composti** (es. `De Gasperi`), con un numero arbitrario di parti separate da spazio

## Regole dei turni

| Turno | Iniziale cognome | Matricola  |
|-------|-----------------|------------|
| 1     | `A-K`           | dispari    |
| **2** | **`A-K`**       | **pari**   |
| **3** | **`L-Z`**       | **dispari**|
| 4     | `L-Z`           | pari       |

## Stati dell'automa

| Stato | Significato                                                      | Accettante |
|-------|------------------------------------------------------------------|------------|
| `0`   | Stato iniziale (spazi iniziali permessi)                         | ❌         |
| `1`   | Ultima cifra della matricola è **dispari**                       | ❌         |
| `2`   | Ultima cifra della matricola è **pari**                          | ❌         |
| `3`   | Spazio dopo matricola **pari** (attende iniziale `A-K`)          | ❌         |
| `4`   | Spazio dopo matricola **dispari** (attende iniziale `L-Z`)       | ❌         |
| `5`   | Cognome in corso (almeno una lettera letta)                      | ✅         |
| `6`   | Spazio dopo cognome (possibile fine o parte successiva)          | ✅         |
| `-1`  | Errore / combinazione non valida                                 | ❌         |

> Gli stati `5` e `6` sono entrambi accettanti: `6` gestisce sia gli spazi finali che i cognomi composti.

## Diagramma delle transizioni

```
 spazio        pari          dispari
  ⟲     ┌────────────[q0]────────────┐
        ▼      pari     dispari      ▼
 spazio(q2)⟷────────────────────── (q1) spazio
   │  └─spazio──►(q3)    (q4)◄─────┘  │
   │              │A-K    L-Z│         │
 A-K              └────┬─────┘       L-Z
   └──────────────────►(q5)◄──────────┘
                    ⟲a-z  │
                    ◄─────┘spazio
                   (q6)⟲spazio
                    │A-Z
                    └──►(q5)
```

In forma tabellare:

| Da \ Input | pari | dispari | `A-K` | `L-Z` | `a-z` | `A-Z` | spazio | altro |
|------------|------|---------|-------|-------|-------|-------|--------|-------|
| `q0`       | `q2` | `q1`    | `-1`  | `-1`  | `-1`  | `-1`  | `q0`   | `-1`  |
| `q1`       | `q2` | `q1`    | `-1`  | `q5`  | `-1`  | `-1`  | `q4`   | `-1`  |
| `q2`       | `q2` | `q1`    | `q5`  | `-1`  | `-1`  | `-1`  | `q3`   | `-1`  |
| `q3`       | `-1` | `-1`    | `q5`  | `-1`  | `-1`  | `-1`  | `-1`   | `-1`  |
| `q4`       | `-1` | `-1`    | `-1`  | `q5`  | `-1`  | `-1`  | `-1`   | `-1`  |
| `q5`       | `-1` | `-1`    | `-1`  | `-1`  | `q5`  | `-1`  | `q6`   | `-1`  |
| `q6`       | `-1` | `-1`    | `q5`  | `q5`  | `-1`  | `q5`  | `-1`   | `-1`  |

## Compilazione ed esecuzione

```bash
javac "PARTE 1/Esercizio 1.4/Esercizio14.java"
java -cp "PARTE 1/Esercizio 1.4" Esercizio14 <stringa>
```

> ⚠️ Passando più argomenti da shell vengono concatenati con spazi — utile per testare stringhe con spazi interni.

## Esempi

```bash
java Esercizio14 "654321 Rossi"        # → OK   (dispari, L-Z → Turno 3)
java Esercizio14 " 123456 Bianchi "    # → OK   (pari, A-K con spazi → Turno 2)
java Esercizio14 "123456De Gasperi"    # → OK   (cognome composto → Turno 2)
java Esercizio14 "123456Bianchi"       # → OK   (senza spazi → Turno 2)
java Esercizio14 "1234 56Bianchi"      # → NOPE (spazio dentro la matricola)
java Esercizio14 "123456Bia nchi"      # → NOPE (spazio dentro la prima parte del cognome)
java Esercizio14 "654322Rossi"         # → NOPE (pari + L-Z → Turno 4)
java Esercizio14 "654321Bianchi"       # → NOPE (dispari + A-K → Turno 1)
```

## Differenze rispetto all'Esercizio 1.3

| Funzionalità                        | Es. 1.3 | Es. 1.4 |
|-------------------------------------|---------|---------|
| Spazi iniziali/finali               | ❌      | ✅      |
| Spazio tra matricola e cognome      | ❌      | ✅      |
| Cognomi composti (es. `De Gasperi`) | ❌      | ✅      |

## Struttura del codice

```
Esercizio 1.4/
└── Esercizio14.java
      ├── scan(String s) → boolean   # logica del DFA
      └── main(String[] args)        # entry point
```
