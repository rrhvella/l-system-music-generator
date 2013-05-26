/*
Copyright (c) 2013, robert.r.h.vella@gmail.com
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/

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