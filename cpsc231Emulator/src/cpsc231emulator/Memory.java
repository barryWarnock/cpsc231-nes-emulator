package cpsc231emulator;

import java.util.function.BiConsumer;

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

    public void listen(int min, int max, BiConsumer<memoryLocation, Memory> lambda);
}

class memoryLocation {
    public int address;
    public short value;
}

class Listener {
    public int min, max;
    BiConsumer<memoryLocation, Memory> lambda;
}