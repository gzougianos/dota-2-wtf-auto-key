package dota2.wtf.auto.key.core;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class AutoKeyBot {
	private static final int DEFAULT_SLEEP_TIME = 20;
	private final List<AutoKeyBotListener> listeners = new LinkedList<AutoKeyBotListener>();
	private final Set<Integer> enabledKeys = ConcurrentHashMap.newKeySet();
	private final AtomicBoolean isCtrlPressed = new AtomicBoolean();
	private final Robot robot;
	private final IsDotaWindowActiveGetter isDotaWindowActiveGetter;
	private final AtomicInteger sleepTime = new AtomicInteger(DEFAULT_SLEEP_TIME);

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

	public int getSleepTime() {
		return sleepTime.get();
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime.set(sleepTime);
	}

	private void pressKeys() throws InterruptedException {
		while (true) {
			sleep();
			if (dotaInactive())
				continue;

			if (isCtrlPressed.get())
				continue;

			for (int nativeKey : enabledKeys) {
				int swingCode = SwingToNativeKeyCodeMappings.getSwingCodeFromNative(nativeKey);

				if (!isCtrlPressed.get() && !dotaInactive()) {
					robot.keyPress(swingCode);
					robot.keyRelease(swingCode);
				}
				sleep();
			}
		}
	}

	private void sleep() throws InterruptedException {
		Thread.sleep(sleepTime.get());
	}

	public void addKeyListener(AutoKeyBotListener listener) {
		listeners.add(listener);
	}

	public void removeKeyListener(AutoKeyBotListener listener) {
		listeners.remove(listener);
	}

	public void enableKey(int nativeKeyCode) {
		if (isEnabled(nativeKeyCode))
			return;

		enableAndNotify(nativeKeyCode);
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

		GlobalScreen.registerNativeHook();

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
