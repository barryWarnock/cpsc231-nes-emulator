package cpsc231emulator;

/**
 * a collection of hardware component classes
 */
public class NES {
    protected Memory mem;
    
    protected CPU_6502 cpu;
    
    public NES() {
        mem = new CPU_Memory();
        cpu = new CPU_6502(mem);
    }
    
    /**
     * parses a INES .nes file and places the contents into mem
     * @param romCode the binary of a .nes file
     * @return true on success or false on failure
     */
    public boolean load_rom(String romCode) {
        
        return false;
    }
}
