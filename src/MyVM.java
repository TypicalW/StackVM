import java.util.*;

public class MyVM {
        //----Instruction set----
        public static final int HALT = 0;
        public static final int PUSH = 1;
        public static final int ADD = 2;
        public static final int SUB = 3;
        public static final int PRINT = 4;

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

                default:
                    throw new RuntimeException("Unknown Instruction "+ instruction);
            }
        }
    }

    // test -- program -- biatch;
    public static void main(String[] args){
        int[] program = {
                PUSH, 10,
                PUSH, 20,
                ADD,
                PRINT,
                HALT
        };
        MyVM vm = new MyVM();
        vm.execute(program);
    }
}