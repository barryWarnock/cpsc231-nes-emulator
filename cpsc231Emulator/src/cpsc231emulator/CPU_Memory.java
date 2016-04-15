package cpsc231emulator;

import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * an implementation of the NES's memory described at https://en.wikibooks.org/wiki/NES_Programming/Memory_Map
 * @author warnock
 */
public class CPU_Memory implements Memory {
    protected short[] memArray;

    public CPU_Memory() {
        memArray = new short[0xFFF];
        //init bitmaps

        int bitmapAddr = 0;

        //0
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xF0));

        //1
        write(bitmapAddr++, (short) (0x20));
        write(bitmapAddr++, (short) (0x60));
        write(bitmapAddr++, (short) (0x20));
        write(bitmapAddr++, (short) (0x20));
        write(bitmapAddr++, (short) (0x70));

        //2
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x10));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0xF0));

        //3
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x10));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x10));
        write(bitmapAddr++, (short) (0xF0));

        //4
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x10));
        write(bitmapAddr++, (short) (0x10));

        //5
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x10));
        write(bitmapAddr++, (short) (0xF0));

        //6
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xF0));

        //7
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x10));
        write(bitmapAddr++, (short) (0x20));
        write(bitmapAddr++, (short) (0x40));
        write(bitmapAddr++, (short) (0x40));

        //8
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xF0));

        //9
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x10));
        write(bitmapAddr++, (short) (0xF0));

        //A
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0x90));

        //B
        write(bitmapAddr++, (short) (0xE0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xE0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xE0));

        //C
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0xF0));

        //D
        write(bitmapAddr++, (short) (0xE0));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0x90));
        write(bitmapAddr++, (short) (0xE0));

        //E
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0xF0));

        //F
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0xF0));
        write(bitmapAddr++, (short) (0x80));
        write(bitmapAddr++, (short) (0x80));
    }

    @Override
    /**
     * @inheritDoc
     */
    public short read(int address) {
        short value = memArray[address];
        return value;
        //TODO input validation
    }

    @Override
    public int read_word(int address) {
        int temp = (memArray[address] << 8);
        temp += memArray[address+1];
        return temp;
    }

    @Override
    public void write(int address, short value) {
        memArray[address] = value;
        //TODO input validation
    }
}