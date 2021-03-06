package cpsc231emulator;

import java.io.File;
import java.io.FileInputStream;

/**
 * a collection of hardware component classes
 */
public class NES {
    protected Memory mem;
    
    protected CPU_6502 cpu;
    
    public NES() {}

    public void init() {
        mem = new CPU_Memory();
        mem.listen(0x2002, 0x2002, (meta, mem) -> {
            if ((meta.value & 0x80) > 0) {
                mem.write(0x2002, (short)(meta.value ^ 0x80));
            }
        });
    }

    /**
     * parses a INES .nes file and places the contents into mem
     * @param filePath the path of the nis file
     * @return true on success or false on failure
     */
    public boolean load_rom(String filePath) {
        try {
            FileInputStream in = new FileInputStream(new File(filePath));
            in.skip(4);
            int prgRomSize = in.read();
            int chrRomSize = in.read();
            int flag6      = in.read();
            int flag7      = in.read();
            int prgRamSize = in.read();
            int flag9      = in.read();
            int flag10     = in.read();
            in.skip(5);

            for (int i = 0; i < prgRomSize * 1024 * 16; i++) {
                short val = (short) in.read();
                if (prgRomSize == 1) {
                    mem.write(0x8000 + i + 0x4000, val);
                }
                mem.write(0x8000 + i, val);
            }
            for (int i = 0; i < chrRomSize * 1024 * 8; i++) {
                in.read();
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void start() {
        cpu = new CPU_6502(mem);
        while (true) {
            cpu.fetch();
            cpu.execute();
        }
    }
}
