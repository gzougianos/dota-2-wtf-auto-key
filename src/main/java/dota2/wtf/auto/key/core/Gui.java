package dota2.wtf.auto.key.core;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;

public class Gui extends JFrame {
	private static final Dimension BUTTON_SIZE = new Dimension(35, 22);
	private static final int GAP_BETWEEN_BUTTONS = 2;
	private final AutoKeyBot keyBot;

	public Gui(AutoKeyBot keyBot) {
		super("Dota 2 WTF AutoKey");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setupIconImage();
		setBackground(new Color(0, 0, 0, 0));
		this.keyBot = keyBot;

		JPanel qwertyButtonsPanel = new JPanel(
				new GridLayout(4, 1, GAP_BETWEEN_BUTTONS, GAP_BETWEEN_BUTTONS));
		qwertyButtonsPanel.setOpaque(false);

		qwertyButtonsPanel.add(createNumbersRow());
		qwertyButtonsPanel.add(createQwerRow());
		qwertyButtonsPanel.add(createAsdfRow());
		qwertyButtonsPanel.add(createZxcvRow());

		setLayout(new BorderLayout());

		add(qwertyButtonsPanel, BorderLayout.CENTER);

		pack();

		placeTopRightOnScreen();
	}

	private void setupIconImage() {
		InputStream imageInputStream = getClass().getClassLoader()
				.getResourceAsStream("favicon_32.png");
		try {
			BufferedImage favicon = ImageIO.read(imageInputStream);
			setIconImage(favicon);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void placeTopRightOnScreen() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int targetX = screenSize.width - getWidth() - 10;
		int targetY = 40;
		setLocation(targetX, targetY);
	}

	private JPanel createNumbersRow() {
		JPanel numbersRowPanel = new JPanel(
				new GridLayout(1, 0, GAP_BETWEEN_BUTTONS, GAP_BETWEEN_BUTTONS));
		numbersRowPanel.setOpaque(false);

		numbersRowPanel.add(createButton(NativeKeyEvent.VC_BACKQUOTE, "`"));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_0));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_1));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_2));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_3));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_4));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_5));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_6));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_7));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_8));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_9));
		numbersRowPanel.add(createButton(NativeKeyEvent.VC_0));
		return numbersRowPanel;
	}

	private JPanel createQwerRow() {
		JPanel outer = new JPanel(new BorderLayout());
		outer.setOpaque(false);

		JButton exitButton = new JButton("Exit");
		exitButton.setFont(exitButton.getFont().deriveFont(Font.BOLD));
		exitButton.setForeground(Color.GREEN.darker());
		exitButton.setFocusPainted(false);
		exitButton.addActionListener(e -> {
			System.exit(0);
		});
		exitButton.setOpaque(false);

		outer.add(exitButton, BorderLayout.CENTER);

		JPanel qwerRow = new JPanel(new GridLayout(1, 0, GAP_BETWEEN_BUTTONS, GAP_BETWEEN_BUTTONS));
		qwerRow.setOpaque(false);

		qwerRow.add(createButton(NativeKeyEvent.VC_Q));
		qwerRow.add(createButton(NativeKeyEvent.VC_W));
		qwerRow.add(createButton(NativeKeyEvent.VC_E));
		qwerRow.add(createButton(NativeKeyEvent.VC_R));
		qwerRow.add(createButton(NativeKeyEvent.VC_T));
		qwerRow.add(createButton(NativeKeyEvent.VC_Y));
		qwerRow.add(createButton(NativeKeyEvent.VC_U));
		qwerRow.add(createButton(NativeKeyEvent.VC_I));
		qwerRow.add(createButton(NativeKeyEvent.VC_O));
		qwerRow.add(createButton(NativeKeyEvent.VC_P));
		qwerRow.add(space());

		outer.add(qwerRow, BorderLayout.LINE_END);
		return outer;
	}

	private JPanel createAsdfRow() {
		JPanel outer = new JPanel(new BorderLayout());
		outer.setOpaque(false);
		outer.add(createClearPanel(), BorderLayout.CENTER);

		JPanel qwerRow = new JPanel(new GridLayout(1, 0, GAP_BETWEEN_BUTTONS, GAP_BETWEEN_BUTTONS));
		qwerRow.setOpaque(false);

		qwerRow.add(createButton(NativeKeyEvent.VC_A));
		qwerRow.add(createButton(NativeKeyEvent.VC_S));
		qwerRow.add(createButton(NativeKeyEvent.VC_D));
		qwerRow.add(createButton(NativeKeyEvent.VC_F));
		qwerRow.add(createButton(NativeKeyEvent.VC_G));
		qwerRow.add(createButton(NativeKeyEvent.VC_H));
		qwerRow.add(createButton(NativeKeyEvent.VC_J));
		qwerRow.add(createButton(NativeKeyEvent.VC_K));
		qwerRow.add(createButton(NativeKeyEvent.VC_L));
		qwerRow.add(space());

		outer.add(qwerRow, BorderLayout.LINE_END);
		return outer;
	}

	private Component createClearPanel() {
		JButton button = new JButton("Clear");
		button.setFocusPainted(false);
		button.setOpaque(false);
		button.addActionListener(e -> {
			keyBot.disableAll();
		});
		return button;
	}

	private JPanel createZxcvRow() {
		JPanel outer = new JPanel(new BorderLayout(GAP_BETWEEN_BUTTONS, GAP_BETWEEN_BUTTONS));
		outer.setOpaque(false);
		outer.add(createCtrlPanel(), BorderLayout.LINE_START);

		JPanel rowAndSpace = new JPanel(new BorderLayout());
		rowAndSpace.setOpaque(false);

		JPanel qwerRow = new JPanel(new GridLayout(1, 0, GAP_BETWEEN_BUTTONS, GAP_BETWEEN_BUTTONS));
		qwerRow.setOpaque(false);

		qwerRow.add(createButton(NativeKeyEvent.VC_Z));
		qwerRow.add(createButton(NativeKeyEvent.VC_X));
		qwerRow.add(createButton(NativeKeyEvent.VC_C));
		qwerRow.add(createButton(NativeKeyEvent.VC_V));
		qwerRow.add(createButton(NativeKeyEvent.VC_B));
		qwerRow.add(createButton(NativeKeyEvent.VC_N));
		qwerRow.add(createButton(NativeKeyEvent.VC_M));

		rowAndSpace.add(qwerRow, BorderLayout.LINE_START);

		rowAndSpace.add(createButton(NativeKeyEvent.VC_SPACE, "Space"), BorderLayout.CENTER);

		outer.add(rowAndSpace, BorderLayout.CENTER);

		return outer;

	}

	private Component createCtrlPanel() {
		JToggleButton button = new JToggleButton("Ctrl");
		button.setEnabled(false);

		Timer timer = new Timer(2, e -> {
			button.setSelected(keyBot.isCtrlDown());
		});

		decorateButton(button);

		// OCD kicks in
		// Without these, layout spikes
		button.setMargin(new Insets(button.getMargin().top, button.getMargin().left - 1,
				button.getMargin().bottom, button.getMargin().right - 1));
		button.setPreferredSize(new Dimension(button.getPreferredSize().width + 4,
				button.getPreferredSize().height));

		timer.start();
		return button;
	}

	private Component space() {
		return Box.createRigidArea(BUTTON_SIZE);
	}

	private JToggleButton createButton(int nativeCode, String customText) {
		JToggleButton b = createButton(nativeCode);
		b.setText(customText);
		return b;
	}

	private JToggleButton createButton(int nativeCode) {
		JToggleButton button = new JToggleButton(NativeKeyEvent.getKeyText(nativeCode));
		button.addActionListener(e -> {
			keyBot.changeKeyState(nativeCode);
		});

		decorateButton(button);

		button.setPreferredSize(BUTTON_SIZE);
		button.setMargin(new Insets(0, 0, 0, 0));
		keyBot.addKeyListener(new AutoKeyBotListener() {

			@Override
			public void keyEnabled(int nativeKeyCode) {
				if (nativeKeyCode == nativeCode)
					button.setSelected(true);
			}

			@Override
			public void keyDisabled(int nativeKeyCode) {
				if (nativeKeyCode == nativeCode)
					button.setSelected(false);
			}
		});
		return button;
	}

	private void decorateButton(AbstractButton button) {
		button.setFocusPainted(false);
		button.addChangeListener(e -> {
			if (button.isSelected()) {
				button.setForeground(Color.red);
				button.setFont(button.getFont().deriveFont(Font.BOLD));
			} else {
				button.setFont(button.getFont().deriveFont(Font.PLAIN));
				button.setForeground(Color.black);
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			installLaf();
			try {
				new Gui(new AutoKeyBot(new JnaUser32DllActiveWindowGetter())).setVisible(true);
			} catch (NativeHookException e) {
				e.printStackTrace();
				showErrorAndExit("Cannot hook to global shortcuts. Process already running?");
			} catch (AWTException e) {
				e.printStackTrace();
				showErrorAndExit("Error creating bot.");
			}
		});
	}

	private static void showErrorAndExit(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private static void installLaf() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
			showErrorAndExit("Look and feel cannot be set");
			System.exit(1);
		}
	}
}
