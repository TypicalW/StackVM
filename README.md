# Stack-Based Bytecode Virtual Machine (Java)

## Overview

This project implements a custom **stack-based bytecode virtual machine (VM)** in Java.

The VM executes integer-based bytecode instructions using:

* A data stack
* A call stack
* A fixed-size memory model
* An instruction pointer
* A structured opcode system
* Custom runtime error handling
* Optional debug tracing mode

The architecture simulates core concepts found in real execution engines and low-level virtual machines.

---

## Architecture

### Core Components

#### 1. Instruction Pointer (IP)

The `ip` (instruction pointer) tracks the current position in the program array.

* Incremented when reading instructions
* Modified explicitly by control flow operations (`JMP`, `CALL`, `RET`, `LOOP`)
* Reset to `0` at the start of each execution

---

#### 2. Data Stack

A `Stack<Integer>` used for:

* Arithmetic operations
* Logical operations
* Comparisons
* Loop counters
* Input handling

All ALU-style instructions operate on the top of this stack.

---

#### 3. Call Stack

A separate `Stack<Integer>` used only for return addresses.

* `CALL` pushes the return address
* `RET` pops and restores the instruction pointer
* Isolated from the data stack to prevent corruption

This separation mirrors real CPU behavior.

---

#### 4. Memory Model

A fixed-size integer array:

```
private final int[] memory = new int[256];
```

Used via:

* `STORE`
* `LOAD`

Includes bounds validation through centralized error handling.

---

#### 5. Custom Error System (VMError)

All runtime errors are centralized in `VMError.java`.

This includes:

* Stack underflow
* Call stack underflow
* Division by zero
* Invalid jump targets
* Invalid memory access
* Unknown instructions
* Invalid input

Errors throw a custom runtime exception:

```
VMError.VMRuntimeException
```

This provides:

* Clean separation of VM logic and error handling
* Consistent error messages
* Extensibility for future improvements

---

## Instruction Set

### Arithmetic

| Opcode | Description    |
| ------ | -------------- |
| ADD    | a + b          |
| SUB    | a - b          |
| MUL    | a * b          |
| DIV    | a / b          |
| MOD    | a % b          |
| NEG    | unary negation |

---

### Stack Manipulation

| Opcode | Description          |
| ------ | -------------------- |
| PUSH   | Push immediate value |
| DUP    | Duplicate top value  |
| SWAP   | Swap top two values  |
| INC    | Increment top value  |
| DEC    | Decrement top value  |

---

### Comparison

| Opcode | Description           |
| ------ | --------------------- |
| EQ     | Equal                 |
| GT     | Greater than          |
| LT     | Less than             |
| GTE    | Greater than or equal |
| LTE    | Less than or equal    |

All comparisons push `1` (true) or `0` (false).

---

### Bitwise Operations

| Opcode | Description |
| ------ | ----------- |
| AND    | a & b       |
| OR     | a | b       |
| XOR    | a ^ b       |
| NOT    | ~a          |
| SHL    | a << b      |
| SHR    | a >> b      |
| USHR   | a >>> b     |

Shifts follow Java `int` semantics (32-bit signed integers).

---

### Control Flow

| Opcode | Description                       |
| ------ | --------------------------------- |
| JMP    | Unconditional jump                |
| JZ     | Jump if zero                      |
| JNZ    | Jump if non-zero                  |
| CALL   | Function call                     |
| RET    | Return from function              |
| LOOP   | Decrement counter and jump if > 0 |

`CALL` and `RET` use a dedicated call stack.

---

### Memory Operations

| Opcode | Description            |
| ------ | ---------------------- |
| STORE  | Store value in memory  |
| LOAD   | Load value from memory |

All memory accesses are bounds-checked.

---

### Input

| Opcode | Description                       |
| ------ | --------------------------------- |
| INPUT  | Reads integer from standard input |

Input is validated using `Scanner.hasNextInt()`.

---

## Debug Mode

The VM includes an optional debug mode.

Enable via:

```
MyVM vm = new MyVM(true);
```

When enabled, the VM prints execution trace before each instruction:

```
IP=0 | INSTR=PUSH | STACK=[] | CALLSTACK=[]
```

This allows:

* Step-by-step state inspection
* Verification of CALL/RET behavior
* Loop tracing
* Stack evolution debugging
* Detection of infinite loops

Debug mode is observational only and does not modify execution state.

---

## Execution Model

Execution follows this pattern:

```
while (ip < program.length) {
    int instruction = program[ip++];
    switch (instruction) {
        ...
    }
}
```

Control flow instructions modify `ip` directly.

`CALL` pushes the next instruction address before jumping.

`RET` restores the instruction pointer from the call stack.

---

## Example Program

```
int[] program = {
    PUSH, 5,
    PUSH, 3,
    ADD,
    PRINT
};
```

Output:

```
8
```

---

## Design Principles

* Stack-based architecture
* Explicit instruction pointer control
* Separated call stack and data stack
* Centralized error management
* Deterministic execution
* Clear opcode organization
* Extensible instruction system

---

## Potential Extensions

* Assembler layer (text-to-bytecode compiler)
* Recursive function demonstrations
* Memory heap model
* Bytecode file loader
* Optimized stack implementation
* Custom instruction categories

---

## Educational Value

This project demonstrates understanding of:

* Virtual machine design
* Instruction decoding
* Stack-based execution models
* Control flow mechanics
* Function call semantics
* Bitwise arithmetic
* Runtime error abstraction
* Execution tracing

---

## Conclusion

This virtual machine simulates core execution engine principles in a simplified, controlled environment. It provides a strong foundation for understanding interpreters, compilers, and low-level runtime systems.

