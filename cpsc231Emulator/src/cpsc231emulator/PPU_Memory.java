package cpsc231emulator;

/**
 * an implementation of the NES's memory described at https://en.wikibooks.org/wiki/NES_Programming/Memory_Map
 * @author bolster
 */
public class PPU_Memory implements Memory {

    protected short[] memArray;

    public PPU_Memory() {
        memArray = new short[0x4000];
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
    /**
     * @inheritDoc
     * magic number time!
     * base    size    purpose
     * $0000  $1000   Pattern Table 0
     * $1000   $1000   Pattern Table 1
     * $2000   $3C0    Name Table 0
     * $23C0   $40 Attribute Table 0
     * $2400   $3C0    Name Table 1
     * $27C0   $40 Attribute Table 1
     * $2800   $3C0    Name Table 2
     * $2BC0   $40 Attribute Table 2
     * $2C00   $3C0    Name Table 3
     * $2FC0   $40 Attribute Table 3
     * $3000   $F00    Mirror of 2000-2EFF
     * $3F00   $10 BG Palette
     * $3F10   $10 Sprite Palette
     * $3F20   $E0 Mirror of 3F00-3F1F
     */
    public void write(int address, short value) {

        //Lowest memory chunks have no mirroring
        if(0x2000 > address) {
            memArray[address] = value;
        }
        // Mirroring of 0x2000 - 0x2EFF
        else if(0x3F00 > address) {
            //There is a small section which is not mirrored
            if(0x2EFF < address && 0x3000 > address) {
                memArray[address] = value;
            }
            //Mirrored part
            else {
                /* baseAddress is the 0x0yyy value */
                int baseAddress = address % 0x1000; //"base" address
                memArray[baseAddress + 0x2000] = value; //section one 0x2yyy
                memArray[baseAddress + 0x3000] = value; //section two 0x3yyy
            } 
        }
        //Mirroring of palette section
        else {
            //TODO input validation
            int baseAddress = (address - 0x3F00) % 0x20;
            /* sections 0x3Fyy -> 2,4,6,8,A,C,E */
            for(int i = 0; 7 > i; i++) {
                memArray[0x3F00 + baseAddress + (i * 0x20)] = value;
            }
        }
    }
}