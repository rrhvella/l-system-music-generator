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

package rrhvella.composition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Generates a MIDI sequence based on the outputs of context-sensitive
 * stochastic L-systems.
 * 
 * The purpose of this class is to generate a score which exhibits strong
 * elements of polyphony as well as musical structure.
 */
public class StructuredPolyphonicLSystemNotator {
	/**
	 * The velocity of the notes in the score.
	 */
	public static final int MEDIUM_VELOCITY = 64;
	/**
	 * The number of tatums in each bar.
	 */
	public static final int BAR_LENGTH = 32;
	/**
	 * The number of crochets in a bar.
	 */
	public static final int CROCHETS_IN_A_BAR = 4;
	/**
	 * The number of tatums in a crochet.
	 */
	public static final int CROCHET_LENGTH = BAR_LENGTH / CROCHETS_IN_A_BAR;
	/**
	 * The minimum number of bars in a phrase.
	 */
	public static final int MIN_PHRASE_LENGTH = 1;
	/**
	 * The maximum number of bars in a phrase.
	 */
	public static final int MAX_PHRASE_LENGTH = 4;
	/**
	 * The minimum number of iterations which will be performed on the L-system
	 * which generates the melody.
	 */
	public static final int MIN_MELODY_ITERATIONS = 2;
	/**
	 * The maximum number of iterations which will be performed on the L-system
	 * which generates the melody.
	 */
	public static final int MAX_MELODY_ITERATIONS = 7;
	/**
	 * The lowest octave which can be occupied by a voice in this piece.
	 */
	public static final int MIN_OCTAVE = 3;
	/**
	 * The number of voices in this piece.
	 */
	private static final int NUMBER_OF_VOICES = 4;

	/**
	 * Analyses the structure string, and uses the other information provided by
	 * the user to generate and organise the tokens for this piece. These tokens
	 * will later be translated into phrases.
	 */
	private static class TokenAnalysis {
		/**
		 * Adds a new note to the specified MIDI track.
		 * 
		 * @param midiTrack
		 *            the track which will be updated.
		 * @param note
		 *            the note which will be added to the track.
		 * @param start
		 *            the start of the note.
		 * @param length
		 *            the length of the note.
		 * @throws InvalidMidiDataException
		 */
		private static void addNote(Track midiTrack, byte note, int start,
				int length) throws InvalidMidiDataException {

			// Add the note on message at the specified starting point.
			ShortMessage noteOnMessage = new ShortMessage();
			noteOnMessage.setMessage(ShortMessage.NOTE_ON, 0, note,
					MEDIUM_VELOCITY);

			midiTrack.add(new MidiEvent(noteOnMessage, start));

			// Add the note off message at the location given by the specified
			// starting point plus the length of the note.
			ShortMessage noteOffMessage = new ShortMessage();
			noteOffMessage.setMessage(ShortMessage.NOTE_OFF, 0, note,
					MEDIUM_VELOCITY);

			midiTrack.add(new MidiEvent(noteOffMessage, start + length));
		}

		/**
		 * The information associated with a single voice.
		 */
		private class VoiceTrack {
			/**
			 * The MIDI track associated with the voice.
			 */
			public Track midiTrack;
			/**
			 * The octave associated with the voice.
			 */
			public int octave;
			/**
			 * The index associated with a voice (used to lookup a melody string
			 * in a track).
			 */
			public int index;
		}

		/**
		 * The meaning of a character in the structure string, as it is
		 * understood by the application.
		 */
		private class Token {
			/**
			 * A state in the rendering process.
			 */
			private class State {
				/**
				 * The current note length.
				 */
				public int noteDuration;
				/**
				 * The current chord index.
				 */
				public int chordDegree;

				public State(int noteLength, int chordIndex) {
					this.noteDuration = noteLength;
					this.chordDegree = chordIndex;
				}
			}

			/**
			 * The length, in bars, for the phrase generated by this token.
			 */
			public int length;
			/**
			 * The chord for each section (equal to 1 crochet) in this phrase.
			 */
			public Chord[] harmonicPattern;
			/**
			 * The melodies for the voices in this phrase.
			 */
			public String[] melodicPatterns;

			/**
			 * Writes the phrase associated with this token, into the track
			 * associated with the given voice, starting from the given index.
			 * 
			 * @param voice
			 *            the voice which will be updated.
			 * @param index
			 *            the start index for the phrase.
			 * @throws InvalidMidiDataException
			 */
			public void updateTrack(VoiceTrack voice, int index)
					throws InvalidMidiDataException {

				/*
				 * Algorithm description:
				 * 
				 * This is an implementation of the enhanced sequential
				 * rendering technique given by Worth and Stepney (2005a, p. 5).
				 * It was further elaborated on their website (2005b,
				 * "Musical Grammars to L-Systems").
				 */

				// The number of tatums which have been processed for the
				// current phrase.
				int phraseLengthProcessed = 0;
				// The harmonic info which is currently being used.
				Chord harmonicInfo = null;
				// The state stack for the sequential rendering process.
				Stack<State> stateStack = new Stack<State>();
				// The first state in the rendering process.
				State currentState = new State(BAR_LENGTH, 0);
				// The string describing the melodic pattern for the current
				// voice, in the current phrase.
				String melodicPattern = melodicPatterns[voice.index];
				// The index of the harmonic info which is currently being used.
				int harmonicIndex = 0;
				// The boundary of the last crochet.
				int lastCrochetBoundary = -1;

				// For each character in the melody string.
				for (char melodicToken : melodicPattern.toCharArray()) {
					// Interpret the token.
					switch (melodicToken) {
					case 'F':
						// If we have crossed the boundary of a crochet, iterate
						// through the harmonic pattern until we come to the
						// chord for the current crochet.
						while (phraseLengthProcessed / CROCHET_LENGTH > lastCrochetBoundary) {
							harmonicInfo = harmonicPattern[harmonicIndex++];
							lastCrochetBoundary++;
						}

						// Add the new note to the track for the current voice.
						// Use the note duration and chord degree specified by
						// the current state.
						addNote(voice.midiTrack, harmonicInfo.getMidiNote(
								currentState.chordDegree, voice.octave), index
								+ phraseLengthProcessed,
								currentState.noteDuration);

						// Update the record of the phrase length processed.
						phraseLengthProcessed += currentState.noteDuration;
						break;

					case '+':
						// Increase the chord index for the current state.
						currentState.chordDegree++;
						break;

					case '-':
						// Decrease the chord index for the current state.
						currentState.chordDegree--;
						break;

					case 'd':
						// Halve the note duration for the current state.
						currentState.noteDuration /= 2;
						break;

					case 'D':
						// Double the note duration for the current state.
						currentState.noteDuration *= 2;
						break;

					case '[':
						// Push the current state onto the stack.
						stateStack.push(new State(currentState.noteDuration,
								currentState.chordDegree));
						break;

					case ']':
						// Pop the previous state from the stack.
						currentState = stateStack.pop();
						break;
					}
				}
			}
		}

		/**
		 * The tonic for this piece.
		 */
		private Note tonic;
		/**
		 * The scale for this piece.
		 */
		private Scale scaleType;
		/**
		 * The tokens generated from analysing the structure string.
		 */
		private Token[] tokens;
		/**
		 * The order of the tokens in the overall structure.
		 */
		private int[] tokenOrder;
		/**
		 * The information for the voices in this piece.
		 */
		private ArrayList<VoiceTrack> voices;
		/**
		 * The random number generator used to simulate stochastic processes in
		 * this system.
		 */
		private Random randomGenerator;

		/**
		 * 
		 * @param tonic
		 *            the tonic for this piece.
		 * @param scaleType
		 *            the scale for this piece.
		 * @param structure
		 *            the structure of this piece, in the format of a string of
		 *            repeating characters ("aba" means
		 *            "first a, then b, then a again").
		 */
		public TokenAnalysis(Note tonic, Scale scaleType, String structure) {
			this.tonic = tonic;
			this.scaleType = scaleType;
			this.randomGenerator = new Random();
			this.tokenOrder = new int[structure.length()];

			// Initialise the voice information array.
			voices = new ArrayList<VoiceTrack>();

			// For each voice.
			for (int voiceIndex = 0; voiceIndex < NUMBER_OF_VOICES; voiceIndex++) {
				// Initialise the voice.
				VoiceTrack voice = new VoiceTrack();

				// Select its octave. The first voice created is given the
				// lowest octave. Subsequent voices are allocated higher octaves
				// in ascending order.
				voice.octave = MIN_OCTAVE + voiceIndex;

				// Store the order of creation for the voice.
				voice.index = voiceIndex;

				// Add the voice to the list.
				voices.add(voice);
			}

			// Maps a token to an integer specifying its order of appearance.
			HashMap<Character, Integer> tokenOrderMap = new HashMap<Character, Integer>();

			// The list of tokens in the string.
			ArrayList<Character> tokenCharacters = new ArrayList<Character>();

			// The order of appearance of the current token.
			int orderOfAppearance = 0;

			// For each character in the string.
			for (int characterIndex = 0; characterIndex < tokenOrder.length; characterIndex++) {
				char character = structure.charAt(characterIndex);

				// If this character has not already appeared, store it as a
				// token, along with its order of appearance.
				if (!tokenOrderMap.containsKey(character)) {
					tokenOrderMap.put(character, orderOfAppearance++);
					tokenCharacters.add(character);
				}

				// Record the order of appearance for the token at this
				// position.
				tokenOrder[characterIndex] = tokenOrderMap.get(character);
			}

			// Initialise the token list.
			tokens = new Token[tokenCharacters.size()];

			// The total length, in bars, of all the tokens.
			int totalLength = 0;

			// For each token.
			for (int tokenIndex = 0; tokenIndex < tokens.length; tokenIndex++) {
				// Initialise it.
				tokens[tokenIndex] = new Token();

				// Randomly generate its length, and record it.
				tokens[tokenIndex].length = randomGenerator
						.nextInt(MAX_PHRASE_LENGTH - MIN_PHRASE_LENGTH + 1)
						+ MIN_PHRASE_LENGTH;

				// Add the length of the current token to the total length.
				totalLength += tokens[tokenIndex].length;
			}

			// The harmonic progression of all the tokens, concatenated
			// together,
			// in order of their appearance.
			LinkedList<Chord> harmony = new LinkedList<Chord>();

			// The L-system which generates the harmony for the scale of the
			// piece's key.
			ContextSensitiveNonDeterministicLSystem chordSystem = scaleType
					.getChordSystem();

			// The length required for the harmony string. This is the total
			// length of all the tokens, in crochets, without the last minim. It
			// is multiplied by three to take into account the format of the
			// text which specifies a chord (ex. '01M').
			int requiredLength = (totalLength * CROCHETS_IN_A_BAR - 2) * 3;

			// The string which describes the harmony of this piece.
			String harmonyString = "";

			// While the harmony string does not meet the required length.
			while (harmonyString.length() < requiredLength) {
				// Reset the harmonic system.
				chordSystem.reset();

				// The string generated by the harmonic system's next iteration.
				String nextString = "";
				// The string generated by the previous iteration.
				String currentString = "";

				// While the harmony string concatenated to the next string does
				// not meet the required length.
				while (harmonyString.length()
						+ (nextString = chordSystem.next()).length() <= requiredLength) {

					// Store this string so that it may be concatenated to the
					// harmony string. This is unless the string that is
					// generated next also meets the length requirements.
					currentString = nextString;
				}

				// Add the longest possible string generated to the harmony
				// string.
				harmonyString += currentString;
			}

			// Add the last two chords of the piece to the harmony string. These
			// should always be the major fifth, so as to ensure an authentic
			// cadence.
			harmonyString += "08M08M";

			// Process the harmony string and generate the chords
			// for the tokens.

			// For each crochet in each token.
			for (int chordIndex = 0; chordIndex < totalLength
					* CROCHETS_IN_A_BAR; chordIndex++) {
				// The position of the current crochet in the harmony string.
				int trueChordIndex = chordIndex * 3;

				// The interval between the tonic of the chord at this location,
				// and the tonic of the key.
				int interval = ((int) harmonyString.charAt(trueChordIndex) - 48)
						* 10
						+ ((int) harmonyString.charAt(trueChordIndex + 1) - 48)
						- 1;

				// The note given by the interval.
				Note note = Note.fromInterval(tonic, interval);

				// The type of the chord at this location.
				ChordType chordType = ChordProfiles
						.chordTypeFromCharacter(harmonyString
								.charAt(trueChordIndex + 2));

				// Add the chord for this crochet.
				harmony.add(new Chord(note, chordType));
			}

			// For each token.
			for (Token token : tokens) {
				// Initialise the chord for the token.
				token.harmonicPattern = new Chord[token.length
						* CROCHETS_IN_A_BAR];
				// Initialise the melodies of the voices in the token.
				token.melodicPatterns = new String[NUMBER_OF_VOICES];

				// The L-system which generates the melodies for the voices.
				ContextSensitiveNonDeterministicLSystem melodyGenerator = new MelodyGenerator(
						token.length);

				// For each crochet in the token.
				for (int crochetIndex = 0; crochetIndex < token.length
						* CROCHETS_IN_A_BAR; crochetIndex++) {
					// Add the harmony for that crochet.
					token.harmonicPattern[crochetIndex] = harmony.poll();
				}

				// For each voice.
				for (int voiceIndex = 0; voiceIndex < NUMBER_OF_VOICES; voiceIndex++) {
					// Randomly select the number of iterations for the melody
					// system.
					int numberOfIterations = randomGenerator
							.nextInt(MAX_MELODY_ITERATIONS
									- MIN_MELODY_ITERATIONS + 1)
							+ MIN_MELODY_ITERATIONS;

					// Iterate through the melody system for the given number of
					// iterations and store the result as the melodic pattern
					// for the current voice and token.
					for (int iterationIndex = 0; iterationIndex < numberOfIterations; iterationIndex++) {
						token.melodicPatterns[voiceIndex] = melodyGenerator
								.next();
					}

					// Reset the melody generator.
					melodyGenerator.reset();
				}
			}
		}

		/**
		 * Generates and returns the MIDI sequence for this piece.
		 * 
		 * @return the MIDI sequence for this piece.
		 * @throws InvalidMidiDataException
		 */
		private Sequence generateSequence() throws InvalidMidiDataException {
			// Initialise the MIDI sequence for this piece. Set its timing
			// unit to "Pulses-Per-Quarter Note" (PPQ). If the PPQ is 4 and an
			// event is added at time step 8, that event will occur after an
			// amount of time, equal to two crochets, has transpired.
			//
			// Set the number of pulses equal to the length of a crochet for
			// this piece.
			Sequence midiSequence = new Sequence(Sequence.PPQ, CROCHET_LENGTH);

			// The index where the next phrase will be inserted.
			int insertIndex = 0;

			// Initialise the tracks for the voices.
			for (VoiceTrack voice : voices) {
				voice.midiTrack = midiSequence.createTrack();
			}

			// For each phrase in the piece.
			for (int tokenIndex : tokenOrder) {
				// Get the token which generates that phrase.
				Token currentToken = tokens[tokenIndex];

				// Add the phrase melody for each voice.
				for (VoiceTrack voice : voices) {
					currentToken.updateTrack(voice, insertIndex);
				}

				// Increment the insert index by the length of the current
				// phrase.
				insertIndex += currentToken.length * BAR_LENGTH;
			}

			// Play the tonic's chord for the last bar.

			// Select the chord for the last bar. Its tonic should be based on
			// the key's tonic.
			Chord lastHarmonicInfo = new Chord(tonic, Enum.valueOf(
					ChordType.class, scaleType.toString() + "_TRIAD"));

			// For each voice.
			for (VoiceTrack voice : voices) {
				// Randomly select the chord degree of the current voice.
				int chordDegree = randomGenerator.nextInt(lastHarmonicInfo
						.getChordPattern().length);

				// Add the note for the current voice.
				addNote(voice.midiTrack,
						lastHarmonicInfo.getMidiNote(chordDegree, voice.octave),
						insertIndex, BAR_LENGTH * 2);
			}

			// Return the MIDI sequence.
			return midiSequence;
		}

	}

	/**
	 * Generate and return a MIDI sequence based on the given parameters.
	 * 
	 * @param tonic
	 *            the tonic for this piece.
	 * @param scaleType
	 *            the scale for this piece.
	 * @param structure
	 *            the structure of this piece, in the format of a string of
	 *            repeating characters ("aba" means
	 *            "first a, then b, then a again").
	 * 
	 * @return the generated MIDI sequence.
	 * @throws InvalidMidiDataException
	 */
	public static Sequence getSequence(Note tonic, Scale scaleType,
			String structure) throws InvalidMidiDataException {

		// Analyse the tokens in the structure.
		TokenAnalysis tokenAnalysis = new TokenAnalysis(tonic, scaleType,
				structure);

		// Generate and return the MIDI sequence.
		return tokenAnalysis.generateSequence();
	}
}