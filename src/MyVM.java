import java.util.*;

public class MyVM {
        //----Instruction set----
        public static final int HALT = 0;
        public static final int PUSH = 1;
        public static final int ADD = 2;
        public static final int SUB = 3;
        public static final int PRINT = 4;
        public static final int MUL = 5;
        public static final int DIV = 6;
        public static final int DUP = 7;
        public static final int SWAP = 8;

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
            //adding new features like MUL, DIV(including 0 error), DUP, SWAP
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
                        throw new RuntimeException("Division by Zero");
                    stack.push(a/b);
                    break;

                case DUP:
                    if(stack.size()<1)
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

                default:
                    throw new RuntimeException("Unknown Instruction "+ instruction);
            }
        }
    }

    // test -- program -- biatch;
    public static void main(String[] args){
        int[] program = {
                PUSH, 5,
                DUP,
                ADD,
                PRINT,
                HALT
        };
        MyVM vm = new MyVM();
        vm.execute(program);
    }
}