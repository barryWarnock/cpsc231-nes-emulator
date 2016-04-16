/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpsc231emulator;

/**
 *
 * @author warnock
 */
public class Main {
    public static void main(String[] args) {
        Device device = new Device();
        device.init();
        String filename = "";
        if (args.length > 0) {
            filename = args[0];
        } else {
            filename = "c8demo.ch8";
        }
        if (device.load_rom(filename)) {
            device.start();
        }
    }
}
