package logger;

public class Log {

	public static boolean debugEnabled = false;
	public static boolean infoEnabled = false;
	
	public static void debug(String str) {
		if(debugEnabled) {
			System.out.println("/debug " + str);
		}
	}
	public static void info(String str) {
		if(infoEnabled) {
			System.out.println("/info " + str);
		}
	}
	public static void warn(String str) {
		if(debugEnabled) {
			System.out.print("/warn " + str);
		}
	}
}
