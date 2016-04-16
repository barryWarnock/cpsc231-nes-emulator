package cpsc231emulator;

import java.io.File;
import java.io.FileInputStream;

/**
 * a collection of hardware component classes
 */
public class Device {
    protected Memory mem;
    
    protected Chip_8 cpu;
    
    public Device() {}

    public void init() {
        mem = new CPU_Memory();
    }

    /**
     * parses a INES .nes file and places the contents into mem
     * @param filePath the path of the nis file
     * @return true on success or false on failure
     */
    public boolean load_rom(String filePath) {
        try {
            FileInputStream in = new FileInputStream(new File(filePath));
            int address = 0x200;

            while(in.available() > 0) {
                short value = (short)in.read();
                mem.write(address++, value);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void start() {
        cpu = new Chip_8(mem);
        long lastTick = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - lastTick > (1000/60)) {
                cpu.step_timers();
                cpu.execute();
                if (cpu.screenChange) {
                    boolean screen[][] = cpu.screen;
                    for (int y = 0; y < screen.length; y++) {
                        for (int x = 0; x < screen[0].length; x++) {
                            char symbol = (screen[y][x]) ? ('#') : (' ');
                            System.out.print(symbol);
                        }
                        System.out.print('\n');
                    }
                    cpu.screenChange = false;
                }
                lastTick = System.currentTimeMillis();
            }
        }
    }
}
