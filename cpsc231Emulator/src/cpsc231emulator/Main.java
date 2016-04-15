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
        if (device.load_rom("Rush_Hour.c8")) {
            device.start();
        }
    }
}
