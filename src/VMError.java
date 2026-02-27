public class VMError {

    public static class VMRuntimeException extends RuntimeException {
        public VMRuntimeException(String message) {
            super(message);
        }
    }

    public static void stackUnderflow(String instruction) {
        throw new VMRuntimeException("Stack Underflow at " + instruction);
    }

    public static void callStackUnderflow(String instruction) {
        throw new VMRuntimeException("CallStack Underflow at " + instruction);
    }

    public static void invalidJump(int target) {
        throw new VMRuntimeException("Invalid jump target " + target);
    }

    public static void invalidMemory(int address) {
        throw new VMRuntimeException("Invalid memory address " + address);
    }

    public static void divisionByZero(String instruction) {
        throw new VMRuntimeException("Division by Zero at " + instruction);
    }
    public static void unknownInstruction(int opcode) {
        throw new VMRuntimeException("Unknown instruction " + opcode);
    }
    public static void invalidInput(String instruction) {
        throw new VMRuntimeException("Wrong datatype at " + instruction);
    }
}