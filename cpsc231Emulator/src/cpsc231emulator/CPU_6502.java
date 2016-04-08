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
    * Stack stack pointer
    * PC program counter
    */
    protected short X, Y, A, Stack;
    protected int PC;
    /*
    * define the flags
    * C carry
    * Z zero
    * I interupt disable
    * D decimal mode (not implemented)
    * B break command
    * V overflow
    * S sign (negative)
    */
    protected boolean C, Z, I, B, V, D, S;
    protected byte cBit = 0b00000001;
    protected byte zBit = 0b00000010;
    protected byte iBit = 0b00000100;
    protected byte bBit = 0b00001000;
    protected byte vBit = 0b00010000;
    protected byte dBit = 0b00100000;
    protected byte sBit = 0b01000000;
    
    public void reset() {
        X = Y = A = 0;
        Stack = 0xFF;
        PC = (mem.read(0xFFFD)*256)+mem.read(0xFFFC); //set PC to reset vector
        /* shouldn't we reset the memory too?*/
        C = Z = I = B = V = D = S = false;
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
            case 0xD8:
                return 2;
            case 0x78:
                return 2;
            case 0xAD:
                return 4;
            case 0x10:
                return 2;
            case 0x00:
                return 7;
            case 0xA0:
                return 2;
            case 0x61:
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
            case 0xd8:
                D = false;
                PC++;
                break;
            case 0x78:
                I = true;
                PC++;
                break;
            case 0xAD:
                A = mem.read(PC+1);
                set_flags(A, (byte)(sBit | zBit));
                PC += 3;
                break;
            case 0x10:
                branch(S, false);
                PC += 2;
                break;
            case 0xA0:
                Y = mem.read(PC+1);
                set_flags(Y, (byte)(sBit | zBit));
                PC+=2;
                break;
            case 0x61: //Add with carry indirect X
                A += mem.read(PC+1 + X);
                set_flags(X, (byte)(sBit | cBit | vBit | zBit));
                PC += 6;
                break;
            case 0x00:
                PC++;
            default:

        }
    }

    protected void set_flags(short value, byte flagBits) {
        Z = ((flagBits & zBit) > 1) ? (value == 0) : (Z);
        S = ((flagBits & sBit) > 1) ? (value < 0) : (S);
        V = ((flagBits & vBit) > 1) ? ((value & 0b10000000) > 1) : (V); //this needs work
        C = ((flagBits & cBit) > 1) ? (value > 0xFF) : (C); //this needs work
    }

    protected void branch(boolean flag, boolean value) {
        if (flag == value) {
            short offset = mem.read(PC + 1);
            if((offset & 0x80) == 0x80)
                PC += (offset-0x80);
            else
                PC += offset;
        }
    }

    /**
     * used to push a value onto the stack, note that this is a utility 
     * function and not an actual instruction
     * @param value the value to push onto the stack
     */
    public void push(short value) {
        mem.write(Stack--, value);
    }
    
    /**
     * used to pop a value from the stack, note that this is a utility 
     * function and not an actual instruction
     * @return 
     */
    public short pop() {
        return mem.read(Stack++);
    }
}
