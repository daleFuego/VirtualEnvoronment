package utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class Recognizer {
	
	private Logger logger;
	private Thread speechThread;
	private Thread resourcesThread;

	public Recognizer(Logger logger){
		this.logger = logger;
		
		configuration = new Configuration();

		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		configuration.setGrammarPath("resource:/grammars");
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);
	}

	private LiveSpeechRecognizer liveSpeechRecognizer;
	private Configuration configuration;

	public void startRecognition(){
		
		try {
			liveSpeechRecognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, null, ex);
		}

		liveSpeechRecognizer.startRecognition(true);

		startSpeechThread();
		startResourcesThread();
	}
	
	@SuppressWarnings("deprecation")
	public void stopRecognition(){
		liveSpeechRecognizer.stopRecognition();
		speechThread.stop();
		logger.log(Level.INFO, "SpeechThread has exited...");
		logger.log(Level.INFO, "Speech recognition was stopped. \n");
	}
	

	private void startSpeechThread() {

		if (speechThread != null && speechThread.isAlive())
			return;

		speechThread = new Thread(() -> {
			logger.log(Level.INFO, "You can start to speak...\n");
			try {
				while (true) {
					SpeechResult speechResult = liveSpeechRecognizer.getResult();

					if (speechResult != null) {
						System.out.println("You said: [" + speechResult.getHypothesis() + "]\n");
					} else
						logger.log(Level.INFO, "I can't understand what you said.\n");

				}
			} catch (Exception ex) {
				logger.log(Level.WARNING, null, ex);
			}

			logger.log(Level.INFO, "SpeechThread has exited...");
		});

		speechThread.start();

	}

	private void startResourcesThread() {

		if (resourcesThread != null && resourcesThread.isAlive())
			return;

		resourcesThread = new Thread(() -> {
			try {
				while (true) {
					Thread.sleep(350);
				}

			} catch (InterruptedException ex) {
				logger.log(Level.WARNING, null, ex);
				resourcesThread.interrupt();
			}
		});

		resourcesThread.start();
	}
}
