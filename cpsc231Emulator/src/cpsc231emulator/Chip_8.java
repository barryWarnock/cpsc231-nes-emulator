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
public class Chip_8 {
    //a reference to the NES's memory
    protected Memory mem;
    /*
    * define the registers
    */
    protected short registers[] = new short[16];
    protected int PC;
    protected int I;
    protected byte SP;
    protected int stack[] = new int[16];
    protected short delayTimer, soundTimer;

    public boolean screen[][] = new boolean[32][64];
    public boolean screenChange = false;
    
    public void reset() {
        screen = new boolean[32][64];
        registers = new short[16];
        PC = 0x200;
        SP = 0;
        stack = new int[16];
        delayTimer = 0;
        soundTimer = 0;
        screenChange = true;
    }
    
    public Chip_8(Memory newMem) {
        mem = newMem;
        reset();
    }
    
    /**
     * executes the next operation and increments the PC
     */
    public void execute() {
        int OPCODE = mem.read_word(PC);
        System.out.println(Integer.toHexString(PC)+": "+Integer.toHexString(OPCODE));
        PC+=2;
        if (OPCODE == 0) {}
        else if (OPCODE == 0x00E0) {
            screen = new boolean[32][64];
            screenChange = true;
        } else if (OPCODE >> 12 == 0x2) {
            push(PC);
            PC = (OPCODE & 0x0FFF);
        } else if ((OPCODE & 0x800E) == 0x800E) {
            int x = ((OPCODE & 0x0F00) >> 8);
            short r = registers[x];
            registers[0xF] = (((0x80 & r) == 0x80)) ? ((short)1) : ((short)0);
            r = (byte)(r << 1);
            registers[x] = r;
        } else if ((OPCODE & 0x8001) == 0x8001) {
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            registers[x] = (short)(registers[x] | registers[y]);
        } else {
            System.out.println("unimplemented: "+Integer.toHexString(OPCODE));
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            PC-=2;
        }
    }
    /**
     * used to push a value onto the stack, note that this is a utility 
     * function and not an actual instruction
     * @param value the value to push onto the stack
     */
    public void push(int value) {
        stack[SP++] = value;
    }
    
    /**
     * used to pop a value from the stack, note that this is a utility 
     * function and not an actual instruction
     * @return 
     */
    public int pop() {
        return stack[SP--];
    }
}
