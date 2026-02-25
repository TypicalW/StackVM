# Stack-Based Virtual Machine in Java

## Overview

This project implements a custom stack-based bytecode virtual machine (VM) in Java.
The VM interprets integer-encoded instructions using a fetch–decode–execute cycle and simulates core CPU architecture concepts including:

* Instruction Pointer (Program Counter)
* Operand Stack
* Memory (RAM)
* Arithmetic and Logical Operations
* Conditional and Unconditional Branching
* Runtime Safety Checks

The design models the behavior of low-level stack machines and demonstrates how interpreters and execution engines operate internally.

---

## Architecture

### Execution Model

The VM follows a classical fetch–decode–execute loop:

1. Fetch instruction at current instruction pointer (IP)
2. Increment IP
3. Decode instruction
4. Execute operation
5. Repeat until HALT or program termination

```java
while (ip < program.length) {
    int instruction = program[ip++];
    switch (instruction) {
        ...
    }
}
```

The instruction pointer is explicitly modified during control flow operations (JMP, JZ, JNZ), enabling branching and looping.

---

## Core Components

### 1. Operand Stack

* Implemented using `Stack<Integer>`
* Used for all arithmetic, logical, and conditional operations
* Follows LIFO semantics
* Enforces runtime safety (stack underflow checks)

### 2. Memory (RAM)

* Fixed size: 256 integer cells
* Indexed addressing
* Accessed exclusively via STORE and LOAD instructions
* Bounds-checked to prevent invalid memory access

```java
private final int[] memory = new int[256];
```

### 3. Instruction Pointer (IP)

* Tracks current execution position
* Automatically increments after each fetch
* Explicitly reassigned during jump instructions

---

## Instruction Set

The VM supports multiple instruction categories.

---

### Core Instructions

| Opcode | Instruction | Description                     |
| ------ | ----------- | ------------------------------- |
| 0      | HALT        | Terminates execution            |
| 1      | PUSH        | Push immediate value onto stack |
| 4      | PRINT       | Pop and print top stack value   |

---

### Arithmetic Instructions

| Opcode | Instruction | Behavior                               |
| ------ | ----------- | -------------------------------------- |
| 2      | ADD         | Push (a + b)                           |
| 3      | SUB         | Push (a - b)                           |
| 5      | MUL         | Push (a * b)                           |
| 6      | DIV         | Push (a / b), division-by-zero checked |
| 9      | MOD         | Push (a % b), division-by-zero checked |
| 10     | NEG         | Push (-a)                              |

All arithmetic operations:

* Require minimum stack size of 2 (except NEG)
* Consume operands
* Push result back to stack

---

### Stack Manipulation

| Opcode | Instruction | Description               |
| ------ | ----------- | ------------------------- |
| 7      | DUP         | Duplicate top stack value |
| 8      | SWAP        | Swap top two stack values |

---

### Comparison Instructions

All comparisons push:

* 1 for true
* 0 for false

| Opcode | Instruction | Behavior      |
| ------ | ----------- | ------------- |
| 41     | EQ          | Push (a == b) |
| 42     | GT          | Push (a > b)  |
| 43     | LT          | Push (a < b)  |
| 44     | GTE         | Push (a >= b) |
| 45     | LTE         | Push (a <= b) |

These integrate directly with conditional branching.

---

### Control Flow Instructions

| Opcode | Instruction | Description            |
| ------ | ----------- | ---------------------- |
| 20     | JMP         | Unconditional jump     |
| 21     | JZ          | Jump if top value == 0 |
| 22     | JNZ         | Jump if top value != 0 |

Behavior:

* Jump target is read from program array
* Bounds-checked before assignment
* JZ and JNZ consume the condition value from stack

Branching is implemented by directly modifying the instruction pointer.

---

### Memory Instructions

| Opcode | Instruction | Description             |
| ------ | ----------- | ----------------------- |
| 31     | STORE       | memory[address] = value |
| 32     | LOAD        | Push memory[address]    |

STORE pops:

* value
* address

LOAD pops:

* address

Both validate memory bounds.

---

## Example Programs

### Example 1: Arithmetic

```java
int[] program = {
    PUSH, 10,
    PUSH, 3,
    MOD,
    PRINT,
    HALT
};
```

Output:

```
1
```

---

### Example 2: Conditional Branching

```java
int[] program = {
    PUSH, 8,
    PUSH, 2,
    LTE,
    JZ, 10,
    PUSH, 999,
    PRINT,
    HALT
};
```

Demonstrates:

* Comparison
* Conditional branching
* Instruction pointer modification

---

### Example 3: Memory Usage

```java
int[] program = {
    PUSH, 5,
    PUSH, 42,
    STORE,

    PUSH, 5,
    LOAD,
    PRINT,
    HALT
};
```

Output:

```
42
```

---

## Error Handling

The VM enforces runtime safety through:

* Stack underflow checks
* Division by zero checks
* Invalid jump target detection
* Memory bounds validation
* Unknown opcode detection

All violations throw RuntimeException.

---

## Design Characteristics

* Deterministic execution model
* Explicit instruction pointer control
* Stack-based evaluation model
* Integer-only architecture
* No implicit side effects
* Clear instruction categorization

The design mirrors classical stack-based virtual machines and demonstrates how interpreters manage execution state.

---

## Potential Extensions

* Text-based assembler (convert assembly to bytecode)
* File-based program loading
* Step-by-step debug mode
* Register-based VM variation
* Call/Return instruction support
* Trace logging mode

---

## How to Run

1. Add instructions inside the `program` array in `main`.
2. Compile and run:

```
javac MyVM.java
java MyVM
```

---

## Purpose

This project explores:

* Virtual machine design
* Interpreter architecture
* Instruction decoding
* Stack-based computation models
* Low-level control flow mechanisms

It serves as a foundational systems programming exercise and a demonstration of execution engine construction in Java.

---
