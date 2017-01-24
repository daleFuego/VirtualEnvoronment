package utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TextAreaHandler extends java.util.logging.Handler {


	private JTextArea textArea;

	public TextAreaHandler(JTextArea textArea) {
		this.textArea = textArea;
	}

    @Override
    public void publish(final LogRecord record) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                StringWriter text = new StringWriter();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                PrintWriter out = new PrintWriter(text);
                out.println(textArea.getText());
                String shortTimeStr = sdf.format(new Date(record.getMillis()));
                out.printf("[%s] %s -> %s", record.getLevel(),
                         shortTimeStr, record.getMessage());
                System.out.println(text.toString());
                textArea.setText(text.toString());
            }

        });
    }

    public JTextArea getTextArea() {
        return this.textArea;
    }

	@Override
	public void close() throws SecurityException {		
	}

	@Override
	public void flush() {		
	}
}