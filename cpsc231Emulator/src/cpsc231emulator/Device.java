package cpsc231emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;

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
        int pixelsize = 8;
        int width = 80*pixelsize;
        int height = 40*pixelsize;
        JFrame frame = new JFrame("jchip");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
        boolean temp[][] = {};
        Buffer buffer = new Buffer(pixelsize);
        frame.add(buffer);

        InputManager input = InputManager.getInstance();
        frame.addKeyListener(input);

        cpu = new Chip_8(mem);
        long lastTick = System.currentTimeMillis();
        long lastCount = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - lastCount > (1000/60)) {
                cpu.step_timers();
                lastCount = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - lastTick > (100000/60000)) {
                cpu.execute();
                if (cpu.screenChange) {
                    buffer.update(cpu.screen);
                    cpu.screenChange = false;
                }
                lastTick = System.currentTimeMillis();
            }
        }
    }
}

class Buffer extends JPanel {
    boolean arr[][];
    int pixelsize;
    public Buffer(int pixelsize) {
        this.pixelsize = pixelsize;
    }

    public void update(boolean[][] screen) {
        arr = screen;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        for (int y = 0; y < arr.length; y++) {
            for (int x = 0; x < arr[0].length; x++) {
                if (arr[y][x]) {
                    g.fillRect(x*pixelsize, y*pixelsize, pixelsize, pixelsize);
                }
            }
        }
    }
}