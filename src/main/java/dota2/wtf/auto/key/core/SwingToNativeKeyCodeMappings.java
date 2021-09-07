package dota2.wtf.auto.key.core;

import java.awt.event.KeyEvent;
import java.util.OptionalInt;

import org.jnativehook.keyboard.NativeKeyEvent;

public class SwingToNativeKeyCodeMappings {

	public static OptionalInt getSwingCodeFromNative(final int nativeCode) {
		int code = getSwingCodeFromNative2(nativeCode);
		if (code == -1)
			return OptionalInt.empty();
		return OptionalInt.of(code);
	}

	/*
	 * From https://github.com/kwhat/jnativehook/issues/106
	 */
	private static int getSwingCodeFromNative2(final int nativeCode) {
		switch (nativeCode) {
			case NativeKeyEvent.VC_1:
				return KeyEvent.VK_1;

			case NativeKeyEvent.VC_2:
				return KeyEvent.VK_2;

			case NativeKeyEvent.VC_3:
				return KeyEvent.VK_3;

			case NativeKeyEvent.VC_4:
				return KeyEvent.VK_4;

			case NativeKeyEvent.VC_5:
				return KeyEvent.VK_5;

			case NativeKeyEvent.VC_6:
				return KeyEvent.VK_6;

			case NativeKeyEvent.VC_7:
				return KeyEvent.VK_7;

			case NativeKeyEvent.VC_8:
				return KeyEvent.VK_8;

			case NativeKeyEvent.VC_9:
				return KeyEvent.VK_9;

			case NativeKeyEvent.VC_0:
				return KeyEvent.VK_0;

			case NativeKeyEvent.VC_A:
				return KeyEvent.VK_A;

			case NativeKeyEvent.VC_B:
				return KeyEvent.VK_B;

			case NativeKeyEvent.VC_C:
				return KeyEvent.VK_C;

			case NativeKeyEvent.VC_D:
				return KeyEvent.VK_D;

			case NativeKeyEvent.VC_E:
				return KeyEvent.VK_E;

			case NativeKeyEvent.VC_F:
				return KeyEvent.VK_F;

			case NativeKeyEvent.VC_G:
				return KeyEvent.VK_G;

			case NativeKeyEvent.VC_H:
				return KeyEvent.VK_H;

			case NativeKeyEvent.VC_I:
				return KeyEvent.VK_I;

			case NativeKeyEvent.VC_J:
				return KeyEvent.VK_J;

			case NativeKeyEvent.VC_K:
				return KeyEvent.VK_K;

			case NativeKeyEvent.VC_L:
				return KeyEvent.VK_L;

			case NativeKeyEvent.VC_M:
				return KeyEvent.VK_M;

			case NativeKeyEvent.VC_N:
				return KeyEvent.VK_N;

			case NativeKeyEvent.VC_O:
				return KeyEvent.VK_O;

			case NativeKeyEvent.VC_P:
				return KeyEvent.VK_P;

			case NativeKeyEvent.VC_Q:
				return KeyEvent.VK_Q;

			case NativeKeyEvent.VC_R:
				return KeyEvent.VK_R;

			case NativeKeyEvent.VC_S:
				return KeyEvent.VK_S;

			case NativeKeyEvent.VC_T:
				return KeyEvent.VK_T;

			case NativeKeyEvent.VC_U:
				return KeyEvent.VK_U;

			case NativeKeyEvent.VC_V:
				return KeyEvent.VK_V;

			case NativeKeyEvent.VC_W:
				return KeyEvent.VK_W;

			case NativeKeyEvent.VC_X:
				return KeyEvent.VK_X;

			case NativeKeyEvent.VC_Y:
				return KeyEvent.VK_Y;

			case NativeKeyEvent.VC_Z:
				return KeyEvent.VK_Z;

			case NativeKeyEvent.VC_BACKQUOTE:
				return KeyEvent.VK_BACK_QUOTE;

			case NativeKeyEvent.VC_SPACE:
				return KeyEvent.VK_SPACE;

			default:
				return -1;
		}
	}

}
