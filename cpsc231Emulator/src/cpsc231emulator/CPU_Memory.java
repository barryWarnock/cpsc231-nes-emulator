package cpsc231emulator;

/**
 * an implementation of the NES's memory described at https://en.wikibooks.org/wiki/NES_Programming/Memory_Map
 * @author warnock
 */
public class CPU_Memory implements Memory{
    protected short[] memArray;
    
    public CPU_Memory() {
        memArray = new short[0x10000];
    }
    
    @Override
    /**
     * @inheritDoc
     */
    public short read(int address) {
        return memArray[address];
        //TODO input validation
    }

    @Override
    public short read_word(int address) {
        short temp = (short) (memArray[++address] << 8);
        temp += memArray[--address];
        return temp;
    }

    @Override
    /**
     * @inheritDoc
     * @author bolster
     * magic number time!
      * base    size    purpose
     * $0000   $800    2KB of work RAM
     * $0800   $800    Mirror of $000-$7FF
     * $1000   $800    Mirror of $000-$7FF
     * $1800   $800    Mirror of $000-$7FF
     * $2000   8       PPU Ctrl Registers
     * $2008   $1FF8   *Mirror of $2000-$2007
     * $4000   $20      Registers (Mostly APU)
     * $4020   $1FDF   Cartridge Expansion ROM
     * $6000   $2000   SRAM
     * $8000   $4000   PRG-ROM
     * $C000   $4000   PRG-RAM
     */
    public void write(int address, short value) {

        //Mirroring of first memory chunk
        if(0x2000 > address) {

            int baseAddress = address % 0x800; //"base" address
            memArray[baseAddress] = value; //first mirror
            memArray[baseAddress + 0x800] = value; //second mirror
            memArray[baseAddress + (0x800 * 2)] = value; //third mirror
            memArray[baseAddress + (0x800 * 3)] = value; //fourth mirror
        }
        //Mirroring of second memory chunk
        else if(0x4000 > address) {
            /* There are only 8 PPU control registers */
            int baseAddress = (address - 0x2000) % 8; //"base" address
            for(int i = 0; 1023 > i; i++) {
                //1023 * 8 = 0x2000 Therefore all of the mirrored values are covered
                memArray[0x2000 + baseAddress + (i * 8)] = value;
            }
        }
        //No mirroring for higher memory locations
        else {
            memArray[address] = value;
            //TODO input validation
        }
    }
}
