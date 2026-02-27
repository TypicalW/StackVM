import java.util.*;
import java.util.Scanner;

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

    //Jump Callstack OPcodes
    public static final int CALl = 51;
    public static final int RET = 52;

    //Loop helper Instructions
    public static final int INC = 60;
    public static final int DEC = 61;
    public static final int LOOP = 62;

    public static final int AND = 70;
    public static final int OR = 71;
    public static final int XOR = 72;
    public static final int NOT = 73;
    public static final int SHL = 74; //Left Shift <<
    public static final int SHR = 75; //Signed Right Shift >>
    public static final int USHR = 76; //Unsigned Right Shift >>>

    //Input Opcode
    public static final int INPUT = 80;


  /*
    * using scanner to add INPUT 80
    * study and add DEBUG mode (idk how to, ill look into it)
    * */


    private final int[] memory = new int[256];
    private final Stack<Integer> stack; //stack as database for instructions;
    private final Stack<Integer> callstack = new Stack<>(); // stack as database for return addresses(ip values)
    private final Scanner scanner = new Scanner(System.in);
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

                case CALl:
                    if(callstack.isEmpty())
                        throw new RuntimeException("CallStack Underflow at CALL");
                    target = program[ip++];
                    callstack.push(ip);
                    ip = target;
                    break;

                case RET:
                    if(callstack.isEmpty())
                        throw  new RuntimeException("CallStack Underflow at RET");
                    int returnAddress = callstack.pop();
                    ip = returnAddress;
                    break;

                case INC:
                    if(stack.isEmpty())
                        throw new RuntimeException("Stack Underflow at INC");
                    a = stack.pop();
                    stack.push(++a);
                    break;

                case DEC:
                    if(stack.isEmpty())
                        throw new RuntimeException("Stack Underflow at DEC");
                    a = stack.pop();
                    stack.push(a-1);
                    break;

                case LOOP:
                    target = program[ip++];
                    if(stack.isEmpty())
                        throw  new RuntimeException("Stack Underflow at LOOP");
                    if (target < 0 || target >= program.length)
                        throw new RuntimeException("Invalid jump target " + target);
                    int counter = stack.pop();
                    if (counter > 0) {
                        counter--;
                        stack.push(counter);
                        ip = target;
                    } else {
                        stack.push(counter);
                    }
                    break;

                case AND:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at AND");
                    b = stack.pop();
                    a = stack.pop();

                    stack.push(a&b);
                    break;

                case OR:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at OR");
                    b = stack.pop();
                    a = stack.pop();

                    stack.push(a|b);
                    break;

                case XOR:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at XOR");
                    b = stack.pop();
                    a = stack.pop();

                    stack.push(a^b);
                    break;

                case NOT:
                    if(stack.isEmpty())
                        throw new RuntimeException("Stack Underflow at NOT");
                    a = stack.pop();

                    stack.push(~a);
                    break;

                case SHL:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at SHL");
                    b = stack.pop();
                    a = stack.pop();

                    stack.push(a<<b);
                    break;

                case SHR:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at SHR");
                    b = stack.pop();
                    a = stack.pop();

                    stack.push(a>>b);
                    break;

                case USHR:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack Underflow at USHR");
                    b = stack.pop();
                    a = stack.pop();

                    stack.push(a>>>b);
                    break;

                case INPUT:
                    if (!scanner.hasNextInt())
                        throw new RuntimeException("Wrong Datatype Input at INPUT");

                    int input = scanner.nextInt();
                    stack.push(input);
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