package ctrls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import utils.CustomOutputStream;
import utils.Recognizer;
import utils.TextAreaHandler;

public class SpeechRecognition extends JFrame {

	private static final long serialVersionUID = -6581723685812995584L;

	private Logger logger = Logger.getLogger(getClass().getName());
	private Recognizer recognizer;

	public SpeechRecognition() {
		setAutoRequestFocus(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Speech Recognition App");
		GridBagLayout gridLayout = new GridBagLayout();
		gridLayout.columnWidths = new int[] { 100 };
		gridLayout.rowHeights = new int[] { 100, 108, 93 };

		getContentPane().setLayout(gridLayout);
		setSize(550, 438);
		setVisible(true);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0.2;
		c.weightx = 0.2;
		c.gridwidth = 0;
		c.gridheight = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		JPanel panelControls = new JPanel();

		getContentPane().add(panelControls, c);
		panelControls
				.setBorder(new TitledBorder(null, "Control Panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelControls.setLayout(new BorderLayout(0, 0));

		JButton btnStart = new JButton("Start");
		panelControls.add(btnStart, BorderLayout.NORTH);

		JButton btnStop = new JButton("Stop");
		panelControls.add(btnStop, BorderLayout.CENTER);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panelControls.add(btnExit, BorderLayout.SOUTH);

		JPanel panelCommands = new JPanel();
		panelCommands.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Available Commands",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelCommands = new GridBagConstraints();
		gbc_panelCommands.anchor = GridBagConstraints.NORTH;
		gbc_panelCommands.gridheight = 2;
		gbc_panelCommands.gridwidth = 0;
		gbc_panelCommands.weighty = 0.4;
		gbc_panelCommands.weightx = 0.4;
		gbc_panelCommands.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelCommands.gridx = 0;
		gbc_panelCommands.gridy = 1;
		getContentPane().add(panelCommands, gbc_panelCommands);
		panelCommands.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPaneCommands = new JScrollPane();
		panelCommands.add(scrollPaneCommands);

		JTextArea listOfCommands = new JTextArea();
		listOfCommands.setEditable(false);
		listOfCommands.setRows(10);
		scrollPaneCommands.setViewportView(listOfCommands);

		JPanel panelConsole = new JPanel();
		panelConsole.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Console",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelConsole = new GridBagConstraints();
		gbc_panelConsole.gridheight = 0;
		gbc_panelConsole.anchor = GridBagConstraints.PAGE_END;
		gbc_panelConsole.weighty = 0.4;
		gbc_panelConsole.weightx = 0.4;
		gbc_panelConsole.gridwidth = 0;
		gbc_panelConsole.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelConsole.gridx = 0;
		gbc_panelConsole.gridy = 2;
		getContentPane().add(panelConsole, gbc_panelConsole);
		panelConsole.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPaneConsole = new JScrollPane();
		panelConsole.add(scrollPaneConsole);

		JTextArea console = new JTextArea();
		console.setEditable(false);
		console.setRows(10);
		scrollPaneConsole.setViewportView(console);

		TextAreaHandler textAreaHandler = new TextAreaHandler(console);
		logger.addHandler(textAreaHandler);
		logger.log(Level.INFO, "Loading..\n");

		PrintStream printStream = new PrintStream(new CustomOutputStream(console));
		System.setOut(printStream);
		System.setErr(printStream);

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recognizer = new Recognizer(logger);
				recognizer.startRecognition();
			}
		});

		fillCommands(listOfCommands);
		setSize(600, 510);
	}

	public static void main(String[] args) {
		new SpeechRecognition();
	}

	private void fillCommands(JTextArea listOfCommands) {
		ArrayList<String> commands = new ArrayList<>();
		String regex = "// CD";

		try {
			Scanner input = new Scanner(
					new File(SpeechRecognition.class.getClassLoader().getResource("grammars/grammar.gram").getFile()));

			while (input.hasNext()) {
				String nextLine = input.nextLine();
				if (nextLine.contains(regex)) {
					commands.add(nextLine);
				}
			}

			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (commands.isEmpty()) {
			logger.log(Level.SEVERE, "There is no command description available !");
		} else {
			for (String cmd : commands) {
				listOfCommands.append(cmd.substring(8) + "\n");
			}
		}
	}
}
