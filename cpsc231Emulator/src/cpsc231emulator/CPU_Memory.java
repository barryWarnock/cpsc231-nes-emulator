/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpsc231emulator;

/**
 * an implementation of the NES's memory described at https://en.wikibooks.org/wiki/NES_Programming/Memory_Map
 * @author warnock
 */
public class CPU_Memory implements Memory{
    protected short[] memArray;
    
    public CPU_Memory() {
        memArray = new short[65536];
    }
    
    @Override
    /**
     * @inheritDoc
     */
    public short read(int address) {
        return memArray[address];
    }

    @Override
    /**
     * @inheritDoc
     * 0x0000-0x07FF is mirrored four times
     * 0x2008-0x2007 is mirrored 1023 times
     */
    public void write(int address, short value) {
        //the 2kb of ram mirrored 4 times
        if (address < 0x2000) {
            /*
            * make the address fit in the first 0x800 of memory (to make 
            * calculating the addresses for the mirrored sections easier)
            */
            while (address > 0x7FF) {
                address -= 0x800;
            }
            for (int i = 0; i < 4; i++) {
                memArray[address + (i*0x800)] = value;
            }
        }
        else if (address < 0x4000) { //address > 0x2000 satisfied by the previous if
            //force memory into the 0x2000-0x2007 range
            while (address > 0x2007) {
                address -= 0x8;
            }
            for (int i = 0; i < 1024; i++) {
                memArray[address + (i*0x800)] = value;
            }
        }
        else {
            memArray[address] = value;
        }
    }
}
