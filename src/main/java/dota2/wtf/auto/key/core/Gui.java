package dota2.wtf.auto.key.core;

import java.awt.AWTException;

import javax.swing.JFrame;

import org.jnativehook.NativeHookException;

public class Gui extends JFrame {

	private AutoKeyBot keyBot;

	public Gui(AutoKeyBot keyBot) {
		super("Dota 2 WTF AutoKey");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.keyBot = keyBot;
	}

	public static void main(String[] args) {
		try {
			new Gui(new AutoKeyBot(new JnaUser32DllActiveWindowGetter())).setVisible(true);
		} catch (NativeHookException | AWTException e) {
			e.printStackTrace();
		}
	}
}
