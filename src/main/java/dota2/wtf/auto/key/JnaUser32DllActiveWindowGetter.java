package dota2.wtf.auto.key;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;

/*
 * Credits: https://stackoverflow.com/questions/6391439/getting-active-window-information-in-java
 */
public class JnaUser32DllActiveWindowGetter implements IsDotaWindowActiveGetter {
	private static final int MAX_TITLE_LENGTH = 1024;

	static class User32DLL {
		static {
			Native.register("user32");
		}

		public static native HWND GetForegroundWindow();

		public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
	}

	@Override
	public boolean isDotaWindowActive() {
		final String windowTitle = getActiveWindowTitle();

		if (windowTitle == null || windowTitle.isEmpty()) {
			return false;
		}

		return windowTitle.trim().toLowerCase().equals("dota 2");
	}

	private static String getActiveWindowTitle() {
		final char[] buffer = new char[MAX_TITLE_LENGTH * 2];
		User32DLL.GetWindowTextW(User32DLL.GetForegroundWindow(), buffer, MAX_TITLE_LENGTH);

		return Native.toString(buffer);
	}

}
