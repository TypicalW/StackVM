import java.util.*;

public class MyVM {
        //----OPCODES----
        // should add MOD(modulus) and NEG(negates the top value)
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

        private Stack<Integer> stack; //stack as database for instructions;
        private int ip; //instruction pointer

    public MyVM(){
        stack = new Stack<>();
        ip = 0;
    }
    public void execute(int[] program){
        while(true){
            int instruction = program[ip];
            ip++;
            //
            switch(instruction){
                case PUSH:
                    int value = program[ip];
                    ip++;
                    stack.push(value);
                    break;

                case ADD:
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a+b);
                    break;

                case SUB:
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(Math.abs(a-b));
                    break;

                case PRINT:
                    System.out.println(stack.pop());
                    break;

                case HALT:
                    return;

                case MUL:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack underflow on MUL");

                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a*b);
                    break;
                case DIV:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack underflow on DIV");
                    b = stack.pop();
                    a= stack.pop();

                    if(b==0)
                        throw new RuntimeException("Division by Zero on DIV");
                    stack.push(a/b);
                    break;

                case DUP:
                    if(stack.isEmpty())
                        throw new RuntimeException("Stack Underflow");
                    stack.push(stack.peek());
                    break;

                case SWAP:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack underflow on SWAP");
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a);
                    stack.push(b);
                    break;

                case MOD:
                    if(stack.size()<2)
                        throw new RuntimeException("Stack underflow on MOD");
                    b = stack.pop();
                    if(b==0)
                        throw new RuntimeException("Division by Zero on MOD");
                    a = stack.pop();
                    stack.push(a%b);
                    break;

                case NEG:
                    if(stack.isEmpty())
                        throw new RuntimeException("Stack underflow on NEG");
                    a = stack.pop();
                    stack.push(-a);
                    break;


                default:
                    throw new RuntimeException("Unknown Instruction "+ instruction);
            }
        }
    }

    // test -- program -- biatch;
    public static void main(String[] args){
        int[] program = {
                PUSH, 5,
                NEG,
                PRINT,
                HALT
        };
        MyVM vm = new MyVM();
        vm.execute(program);
    }
}