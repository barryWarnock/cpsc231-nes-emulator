package cpsc231emulator;

/**
 * the memory interface gives us a way to access the underlying
 * array representing our memory in a way that reflects the
 * memory mirroring and other oddities of NES memory
 * @author warnock
 */
public interface Memory {
    /**
     * accesses the data stored at the given addresses
     * @param address the address of the data you want
     * @return the data stored at address
     */
    public short read(int address);
    
    /**
     * writes the given value at the given address
     * @param address the address to write value to
     * @param value the value to write at address
     */
    public void write(int address, short value);

    public short read_word(int address);
}
