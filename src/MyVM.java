import java.util.*;

public class MyVM {
    //----OPCODES---
    public static final int HALT = 0;
    public static final int PUSH = 1;
    public static final int ADD = 2;
    public static final int SUB = 3;
    public static final int PRINT = 4;
    public static final int MUL = 5;
    public static final int DIV = 6;
    public static final int DUP = 7;
    public static final int SWAP = 8;
    public static final int MOD = 9;
    public static final int NEG = 10;


    //Jump OP codes
    public static final int JMP = 20;
    public static final int JZ = 21;
    public static final int JNZ = 22;

    //RAM OP codes
    public static final int STORE = 31;
    public static final int LOAD = 32;

    //Comparison Instructions OPCodes
    public static final int EQ = 41;
    public static final int GT = 42;
    public static final int LT = 43;
    public static final int GTE = 44;
    public static final int LTE = 45;


    private final int[] memory = new int[256];
    private final Stack<Integer> stack; //stack as database for instructions;
    private int ip; //instruction pointer

    public MyVM() {
        stack = new Stack<>();
        ip = 0;
        stack.clear();
    }

    public void execute(int[] program) {
        while (ip<program.length) {
            int instruction = program[ip++];
            // switch cases 
            switch (instruction) {
                case PUSH:
                    int value = program[ip];
                    ip++;
                    stack.push(value);
                    break;

                case ADD:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack underflow at ADD");
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a + b);
                    break;

                case SUB:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack underflow at SUB");
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a - b);
                    break;

                case PRINT:
                    System.out.println(stack.pop());
                    break;

                case HALT:
                    return;

                case MUL:
                    if (stack.size() < 2)
                        throw new RuntimeException("Stack underflow on MUL");

                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a * b);
                    break;
                case DIV:
                    if (stack.size() < 2)
                        throw new RuntimeException("Stack underflow on DIV");
                    b = stack.pop();
                    a = stack.pop();

                    if (b == 0)
                        throw new RuntimeException("Division by Zero on DIV");
                    stack.push(a / b);
                    break;

                case DUP:
                    if (stack.isEmpty())
                        throw new RuntimeException("Stack Underflow");
                    stack.push(stack.peek());
                    break;

                case SWAP:
                    if (stack.size() < 2)
                        throw new RuntimeException("Stack underflow on SWAP");
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(b);
                    stack.push(a);
                    break;

                case MOD:
                    if (stack.size() < 2)
                        throw new RuntimeException("Stack underflow on MOD");
                    b = stack.pop();
                    if (b == 0)
                        throw new RuntimeException("Division by Zero on MOD");
                    a = stack.pop();
                    stack.push(a % b);
                    break;

                case NEG:
                    if (stack.isEmpty())
                        throw new RuntimeException("Stack underflow on NEG");
                    a = stack.pop();
                    stack.push(-a);
                    break;

                case JMP:
                    int target = program[ip++];
                    if (target < 0 || target >= program.length)
                        throw new RuntimeException("Invalid jump exception " + target);
                    ip = target;
                    break;

                case JZ:
                    if (stack.isEmpty())
                        throw new RuntimeException("Stack underflow on JZ");
                    target = program[ip++];
                    value = stack.pop();
                    if (value == 0) {
                        if (target < 0 || target >= program.length)
                            throw new RuntimeException("Invalid Jump target " + target);
                        ip = target;
                    }
                    break;

                case JNZ:
                    if (stack.isEmpty())
                        throw new RuntimeException("Stack underflow on JNZ");
                    target = program[ip++];
                    value = stack.pop();
                    if (value != 0) {
                        if (target < 0 || target >= program.length)
                            throw new RuntimeException("Invalid Jump target " + target);
                        ip = target;
                    }
                    break;

                case STORE:
                    if (stack.size() < 2)
                        throw new RuntimeException("Stack Underflow at Store");
                    value = stack.pop();
                    int address = stack.pop();

                    if (address < 0 || address >= memory.length)
                        throw new RuntimeException("Invalid memory address " + address);
                    memory[address] = value;
                    break;


                case LOAD:
                    if (stack.isEmpty())
                        throw new RuntimeException("Stack underflow at LOAD");
                    address = stack.pop();
                    if (address < 0 || address >= memory.length)
                        throw new RuntimeException("Invalid memory address " + address);
                    stack.push(memory[address]);
                    break;

                case EQ:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack underflow at EQ");
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a==b?1:0);
                    break;

                case GT:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at GT");
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a>b?1:0);
                    break;

                case LT:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at LT");
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a<b?1:0);
                    break;

                case GTE:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at GTE");
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a>=b?1:0);
                    break;

                case LTE:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at LTE");
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a<=b?1:0);
                    break;

                default:
                    throw new RuntimeException("Unknown Instruction " + instruction);
            }
        }
    }


    /* test -- program -- biatch;*/
    public static void main(String[] args) {
        int[] program = {
                //Add your OPCodes here for testing
        };
        MyVM vm = new MyVM();
        vm.execute(program);
    }
}