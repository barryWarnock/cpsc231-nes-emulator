package cpsc231emulator;

/**
 * an implementation of the MOS 6502 processor used in the NES which
 * had none of the 6502's BCD operations
 * documentation of the 6502's opcodes can be found at the following links:
 * http://6502.org/tutorials/6502opcodes.html
 * http://www.visual6502.org/wiki/index.php?title=6502_all_256_Opcodes
 * other useful documents at:
 * https://en.wikipedia.org/wiki/MOS_Technology_6502#Registers
 * http://nesdev.com/NESDoc.pdf
 */
public class CPU_6502 {
    //a reference to the NES's memory
    protected Memory mem;
    /*
    * define the registers
    * X,Y 8 bit index registers
    * S stack pointer
    * PC program counter
    */
    protected short X, Y, A, S;
    protected int PC;
    /*
    * define the flags
    * C carry
    * Z zero
    * I interupt disable
    * D decimal mode (not implemented)
    * B break command
    * V overflow
    */
    protected boolean C, Z, I, B, V;
    
    public void reset() {
        X = Y = A = 0;
        S = 0xFF;
        PC = (mem.read(0xFFFD)*256)+mem.read(0xFFFC); //set PC to reset vector
        /* shouldn't we reset the memory too?*/
        C = Z = I = B = V = false;
    }
    
    public CPU_6502(Memory newMem) {
        mem = newMem;
        reset();
    }
    
    /**
     * returns the number of clock cycles the next instruction will take
     * this is important because the PPU (picture processing unit)
     * runs along side the cpu so they need to stay in sync
     * @return the number of clock cycles the next operation will take
     */
    public byte fetch() {
        switch (mem.read(PC)) {
            case 0x00:
                return 0;
            case 0x01:
                return 6;
            default:
                System.out.println("Unimplemented OP: "+Integer.toHexString(mem.read(PC)));
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return -1;
        } //TODO write a script to generate the cases for all opcodes (unless someone wants to manually copy the 150+ permutations)
        //this should probably actually end up in a config file that gets loaded into a map but we can talk about that later
    }
    
    /**
     * executes the next operation and increments the PC
     */
    public void execute() {
        switch (mem.read(PC)) {
            case 0x00:
                op_00();
                break;
            default:
                //log unimplemented op-code
        }
    }
    
    /**
     * used to push a value onto the stack, note that this is a utility 
     * function and not an actual instruction
     * @param value the value to push onto the stack
     */
    public void push(short value) {
        mem.write(S--, value);
    }
    
    /**
     * used to pop a value from the stack, note that this is a utility 
     * function and not an actual instruction
     * @return 
     */
    public short pop() {
        return mem.read(S++);
    }
    
    /**
     * BRK causes a non-maskable interrupt and increments the program counter by 
     * one. Therefore an RTI will go to the address of the BRK +2 so that BRK may 
     * be used to replace a two-byte instruction for debugging and the subsequent 
     * RTI will be correct.
     */
    protected void op_00() {
        PC++;
    }
}
