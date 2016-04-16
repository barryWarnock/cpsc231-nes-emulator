package cpsc231emulator;

import java.util.Random;

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
    Random rand = new Random();
    InputManager input = new InputManager();
    //top row is 5-8
    int[] keymap = {53, 54, 55, 56,
            116, 121, 117, 105,
            103, 104, 106, 107,
            118, 98, 110, 109};


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

    public void step_timers() {
        if (delayTimer > 0) {
            delayTimer--;
        }
        if (soundTimer > 0) {
            soundTimer--;
            if (soundTimer == 0) {
                System.out.println("\007");
            }
        }
    }

    /**
     * executes the next operation and increments the PC
     */
    public void execute() {
        int OPCODE = mem.read_word(PC);
        System.out.println(Integer.toHexString(PC)+": "+Integer.toHexString(OPCODE));
        PC+=2;
        if (OPCODE == 0) {}
        else if (OPCODE == 0x00E0) { //0x00E0
            screen = new boolean[32][64];
            screenChange = true;
        } else if (OPCODE == 0x00EE) { //0x00EE
            PC = pop();
        } else if (OPCODE >> 12 == 0x1) { //0x1nnn
            PC = OPCODE & 0x0FFF;
        } else if (OPCODE >> 12 == 0x2) { //0x2nnn
            push(PC);
            PC = (OPCODE & 0x0FFF);
        } else if (OPCODE >> 12 == 0x3) { //0x3xkk
            int x = (OPCODE & 0x0F00) >> 8;
            if (registers[x] == (OPCODE & 0x00FF)) {
                PC+=2;
            }
        } else if (OPCODE >> 12 == 0x4) { //4xkk
            int x = (OPCODE & 0x0F00) >> 8;
            if (registers[x] != (OPCODE & 0x00FF)) {
                PC+=2;
            }
        } else if ((OPCODE >> 12 == 0x5) && ((OPCODE & 0x000F) == 0x0)) {//5xy0
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            if (registers[x] == registers[y]) {
                PC+=2;
            }
        } else if (OPCODE >> 12 == 0x6) { //6xkk
            int x = ((OPCODE & 0x0F00) >> 8);
            registers[x] = (short)(OPCODE & 0x00FF);
        } else if (OPCODE >> 12 == 0x7) { //7xkk
            int x = ((OPCODE & 0x0F00) >> 8);
            registers[x] += (short)(OPCODE & 0x00FF);
            registers[x] = (short)(0xff & registers[x]);
        } else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0x0)) { //8xy0
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            registers[x] = registers[y];
        } else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0x2)) { //8xy2
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            registers[x] = (byte)(registers[x] & registers[y]);
        } else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0x3)) { //8xy3
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            registers[x] = (byte)(registers[x] ^ registers[y]);
        } else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0x4)) { //8xy4
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            registers[x] += (registers[0xF] == 1) ? (1) : (0);
            registers[0xF] = (registers[x] + registers[y] > 255) ? (short)(1) : (short)(0);
            registers[x] = (byte)(registers[x] + registers[y]);
        } else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0x5)) { //8xy5
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            registers[x] -= (registers[0xF] == 1) ? (1) : (0);
            registers[0xF] = (registers[x] > registers[y]) ? (short)(1) : (short)(0);
            registers[x] = (byte)(registers[x] - registers[y]);
        }  else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0x6)) { //8xy6
            int x = ((OPCODE & 0x0F00) >> 8);
            registers[0xF] = ((registers[x] & 1) == 1) ? (short)(1) : (short)(0);
            registers[x] = (byte)(registers[x] >> 1);
        }  else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0x7)) { //8xy7
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            registers[x] -= (registers[0xF] == 1) ? (1) : (0);
            registers[0xF] = (registers[x] < registers[y]) ? (short)(1) : (short)(0);
            registers[x] = (byte)(registers[y] - registers[x]);
        } else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0xE)) {//0x8xyE
            int x = ((OPCODE & 0x0F00) >> 8);
            short r = registers[x];
            registers[0xF] = (((0x80 & r) == 0x80)) ? ((short)1) : ((short)0);
            r = (byte)(r << 1);
            registers[x] = r;
        } else if ((OPCODE >> 12 == 0x8) && ((OPCODE & 0x000F) == 0x1)) { //8xy1
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            registers[x] = (short)(registers[x] | registers[y]);
        } else if ((OPCODE >> 12 == 0x9) && ((OPCODE & 0x000F) == 0x0)) { //9xy0
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            if (registers[x] != registers[y]) {
                PC+=2;
            }
        } else if (OPCODE >> 12 == 0xA) { //Annn
            I = OPCODE & 0x0FFF;
        } else if (OPCODE >> 12 == 0xB) { //Bnnn
            PC = (OPCODE & 0x0FFF) + registers[0];
        } else if (OPCODE >> 12 == 0xC) { //cxnn
            int x = ((OPCODE & 0x0F00) >> 8);
            registers[x] = (short)(rand.nextInt(256) & (OPCODE & 0x00FF));
        } else if (OPCODE >> 12 == 0xD) { //dxyn
            int address = I;
            int x = ((OPCODE & 0x0F00) >> 8);
            int y = ((OPCODE & 0x00F0) >> 4);
            int n = (OPCODE & 0x000F);
            short overwrite = 0;

            for (int i = 0; i < n; i++) {
                short bitmask = mem.read(address + n);
                String bits = Integer.toBinaryString(bitmask);
                String leftpad = "";
                for (int l = bits.length(); l < 8; l++) {
                    leftpad += "0";
                }
                bits = leftpad+bits;
                char[] bitArr = bits.toCharArray();
                for (int j = 0; j < 8; j++) {
                    int X = (x+j) % 64;
                    int Y = (y+i) % 32;
                    boolean bitVal = (Integer.parseInt(bitArr[j]+"")==1);

                    if (screen[Y][X] && bitVal) {
                        overwrite = 1;
                    }
                    screen[Y][X] = screen[Y][X] ^ bitVal;
                }
            }
            registers[0xF] = overwrite;
            screenChange = true;
        } else if (((OPCODE >> 12 == 0xE) && ((OPCODE & 0x00FF) == 0x9E))) { //ex9e
            //top row is 5-8
            int[] keymap = {53, 54, 55, 56,
                            116, 121, 117, 105,
                            103, 104, 106, 107,
                            118, 98, 110, 109};

            int key = ((OPCODE & 0x0F00) >> 8);
            if (input.isKeyDown(keymap[key])) {
                PC+=2;
            }
        } else if (((OPCODE >> 12 == 0xE) && ((OPCODE & 0x00FF) == 0xA1))) { //exA1
            int key = ((OPCODE & 0x0F00) >> 8);
            if (input.isKeyUp(keymap[key])) {
                PC+=2;
            }
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x07))) { //fx07
            int x = ((OPCODE & 0x0F00) >> 8);
            registers[x] = delayTimer;
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x0a))) { //fx0a
            int pressedKey = -1;
            while(pressedKey == -1) {
                for (int i = 0; i < 16; i++) {
                    if (input.isKeyDown(keymap[i])) {
                        pressedKey = i;
                        break;
                    }
                }
            }

            int x = ((OPCODE & 0x0F00) >> 8);
            registers[x] = (short)pressedKey;
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x015))) { //fx15
            int x = ((OPCODE & 0x0F00) >> 8);
            delayTimer = registers[x];
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x018))) { //fx18
            int x = ((OPCODE & 0x0F00) >> 8);
            soundTimer = registers[x];
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x01E))) { //fx1e
            int x = ((OPCODE & 0x0F00) >> 8);
            I += registers[x];
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x029))) { //fx29
            int x = ((OPCODE & 0x0F00) >> 8);
            int val = registers[x];
            I = ((val-1) * 8);
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x033))) { //fx33
            int x = ((OPCODE & 0x0F00) >> 8);
            String bcdString = Integer.toString(registers[x]);
            String leftpad = "";
            for (int l = bcdString.length(); l < 3; l++) {
                leftpad += "0";
            }
            char[] bcd = (leftpad+bcdString).toCharArray();
            for (int i = 0; i < 3; i++) {
                mem.write(I+i, (short)bcd[i]);
            }
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x055))) { //fx55
            int x = ((OPCODE & 0x0F00) >> 8);
            for (int i = 0; i <= x; i++) {
                mem.write(I+i, registers[i]);
            }
        } else if (((OPCODE >> 12 == 0xF) && ((OPCODE & 0x00FF) == 0x065))) { //fx65
            int x = ((OPCODE & 0x0F00) >> 8);
            for (int i = 0; i <= x; i++) {
                registers[i] = mem.read(I+i);
            }
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
        return stack[--SP];
    }
}
