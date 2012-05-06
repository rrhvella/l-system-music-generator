import java.io.File;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import rrhvella.composition.Note;
import rrhvella.composition.Scale;
import rrhvella.composition.StructuredPolyphonicLSystemNotator;

/**
 * Contains the main method.
 * 
 * Main is responsible for calling the classes necessary to generate the MIDI
 * sequence, playing it, and storing it in a file.
 * 
 */
public class Main {
	/**
	 * The main method for this project.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		try {
			// If not enough arguments have been specified.
			if (args.length < 4) {
				// Inform the user of the arguments she needs to specify, and
				// the domain for each argument.
				System.out
						.println("Please enter the following arguments for this program "
								+ "(seperated by spaces): ");
				System.out
						.println("Argument 1: The tonic, ex. A ASHARP B BFLAT");
				System.out
						.println("Argument 2: The scale (either MAJOR or MINOR)");
				System.out
						.println("Argument 3: The structure of the piece, ex. ababcd "
								+ "(Note: tokens can be anything, as long as they are "
								+ "a single ascii character)");
				System.out
						.println("Argument 4: The name of the piece, ex. \"La la la\" "
								+ "(Note: this is used as the name of the midi file.)");
				System.out.println("");
				System.out
						.println("The following would be valid calls to this program:");
				System.out.println("java Main ASHARP MINOR abaaaabc Test1 ");
				System.out.println("java Main C MAJOR AAAA \"Test 2\"");
				System.out.println("java Main EFLAT MINOR 4432 Test3");

				return;
			}

			// Generate the MIDI sequence.
			Sequence midiSequence = StructuredPolyphonicLSystemNotator
					.getSequence(
							Enum.valueOf(Note.class, args[0].toUpperCase()),
							Enum.valueOf(Scale.class, args[1].toUpperCase()),
							args[2]);

			// Play the MIDI sequence.
			Sequencer midiSequencer = MidiSystem.getSequencer(true);
			midiSequencer.setSequence(midiSequence);
			midiSequencer.open();
			midiSequencer.start();

			// Force the current thread to sleep until the sequence has been
			// played.
			Thread.sleep(midiSequence.getMicrosecondLength() / 1000);
			midiSequencer.close();

			// Write the sequence to a type 1 file.
			MidiSystem.write(midiSequence, 1, new File(args[3] + ".mid"));
		} catch (Exception e) {
			// If an exception occurs, print the stack trace to the user.
			e.printStackTrace();
		}

	}
}