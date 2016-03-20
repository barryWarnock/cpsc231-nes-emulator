import logger.Log;

/** @author Bolster
 *
 *
 */
public class CPU_Operations {
	
	/**
	 * This method will essentially be a gigantic switch statement.
	 * 
	 *
	 */
	public static void perform(int code) {
		switch(code) {
			/* This first set of instructions is branches.
			 *
			 *
			 */
			case 0x10: // BPL (Branch on PLus)
				Log.debug("BPL was executed. " + code);
				break;
			case 0x30: // BMI (Branch on MInus)
				Log.debug("BMI was executed. " + code);
				break;
			case 0x50: // BVC (Branch on oVerflow Clear)
				Log.debug("BVC was executed. " + code);
				break;
			case 0x70: // BVS (Branch on oVerflow Set)
				Log.debug("BVS was executed. " + code);
				break;
			case 0x90: // BCC (Branch on Carry Clear)
				Log.debug("BCC was executed. " + code);
				break;
			case 0xB0: // BCS (Branch on Carry Set)
				Log.debug("BCS was executed." + code);
				break;
			case 0xD0: // BNE (Branch on Not Equal)
				Log.debug("BNE was executed. " + code);
				break;
			case 0xF0: // BEQ (Branch on EQual)
				Log.debug("BEQ was executed. " + code);
				break;
			/* Next set of instructions
			 *
			 *
			 */


			case 0xEA: // NOP
				Log.debug("NOP was executed. " + code);
				break;
			default:
				Log.warn("Default case was hit! " + code);
		}
	}
}