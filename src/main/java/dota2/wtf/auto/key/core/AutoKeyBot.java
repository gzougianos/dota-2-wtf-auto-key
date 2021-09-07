package dota2.wtf.auto.key.core;

import static dota2.wtf.auto.key.core.SwingToNativeKeyCodeMappings.getSwingCodeFromNative;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class AutoKeyBot {
	private static final int SLEEP_TIME_BETWEEN_PRESSES = 20;
	private final List<AutoKeyBotListener> listeners = new LinkedList<AutoKeyBotListener>();
	private final Set<Integer> enabledKeys = ConcurrentHashMap.newKeySet();
	private final AtomicBoolean isCtrlPressed = new AtomicBoolean();
	private final Robot robot;
	private final IsDotaWindowActiveGetter isDotaWindowActiveGetter;

	public AutoKeyBot(IsDotaWindowActiveGetter isDotaWindowActiveGetter)	throws NativeHookException,
																			AWTException
	{
		this.isDotaWindowActiveGetter = isDotaWindowActiveGetter;
		registerNativeHook();
		robot = new Robot();
		Thread pressingThread = new Thread(() -> {
			try {
				pressKeys();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		pressingThread.start();
	}

	public void disableAll() {
		Iterator<Integer> iterator = enabledKeys.iterator();
		while (iterator.hasNext()) {
			Integer nativeCode = iterator.next();
			disableAndNotify(nativeCode);
		}
	}

	private void pressKeys() throws InterruptedException {
		while (true) {
			sleep();
			if (dotaInactive())
				continue;

			if (isCtrlPressed.get())
				continue;

			for (int nativeKey : enabledKeys) {
				OptionalInt swingCode = getSwingCodeFromNative(nativeKey);

				if (!swingCode.isPresent())
					continue;

				if (!isCtrlPressed.get() && !dotaInactive()) {
					robot.keyPress(swingCode.getAsInt());
					robot.keyRelease(swingCode.getAsInt());
				}
				sleep();
			}
		}
	}

	public boolean isCtrlDown() {
		return isCtrlPressed.get();
	}

	private void sleep() throws InterruptedException {
		Thread.sleep(SLEEP_TIME_BETWEEN_PRESSES);
	}

	public void addKeyListener(AutoKeyBotListener listener) {
		listeners.add(listener);
	}

	public void removeKeyListener(AutoKeyBotListener listener) {
		listeners.remove(listener);
	}

	public void changeKeyState(int nativeKeyCode) {
		changeStateAndNotify(nativeKeyCode);
	}

	private void changeStateAndNotify(int nativeKeyCode) {
		if (isEnabled(nativeKeyCode)) {
			disableAndNotify(nativeKeyCode);
			return;
		}

		enableAndNotify(nativeKeyCode);
	}

	private void disableAndNotify(int nativeKeyCode) {
		enabledKeys.remove(nativeKeyCode);
		listeners.forEach(listener -> listener.keyDisabled(nativeKeyCode));
	}

	private void enableAndNotify(int nativeKeyCode) {
		enabledKeys.add(nativeKeyCode);
		listeners.forEach(listener -> listener.keyEnabled(nativeKeyCode));
	}

	private boolean isEnabled(int nativeKeyCode) {
		return enabledKeys.contains(nativeKeyCode);
	}

	private void registerNativeHook() throws NativeHookException {
		LogManager.getLogManager().reset();

		// Get the logger for "org.jnativehook" and set the level to off.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);

		try {
			GlobalScreen.registerNativeHook();
		} catch (UnsatisfiedLinkError e) {
			throw new NativeHookException(e);
		}

		//@formatter:off
		GlobalScreen.addNativeKeyListener(new KeyListener());
		//@formatter:on
	}

	private boolean dotaInactive() {
		return !isDotaWindowActiveGetter.isDotaWindowActive();
	}

	private class KeyListener implements NativeKeyListener {

		@Override
		public void nativeKeyPressed(NativeKeyEvent e) {
			if (dotaInactive()) {
				return;
			}

			if (isCtrl(e)) {
				isCtrlPressed.set(true);
			}
		}

		@Override
		public void nativeKeyReleased(NativeKeyEvent e) {
			if (dotaInactive())
				return;

			if (isCtrl(e)) {
				isCtrlPressed.set(false);
				return;
			}

			if (isCtrlPressed.get()) {
				changeStateAndNotify(e.getKeyCode());
			}
		}

		// For some reason e.getKeyCode() always returns 0 here
		@Override
		@Deprecated
		public void nativeKeyTyped(NativeKeyEvent e) {
		}

		private boolean isCtrl(NativeKeyEvent ev) {
			return ev.getKeyCode() == NativeKeyEvent.VC_CONTROL_L
					|| ev.getKeyCode() == NativeKeyEvent.VC_CONTROL_R;
		}
	}

}
