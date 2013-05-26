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

/**
 * The harmonic information occupying a span of time.
 */
public class Chord {
	/**
	 * The number of notes in an octave.
	 */
	public static final int NOTES_IN_AN_OCTAVE = 12;

	/**
	 * The maximum MIDI note.
	 */
	private static final int MAX_MIDI = 127;
	/**
	 * The MIDI note equivalent to the chord's tonic.
	 */
	private byte tonic;
	/**
	 * The interval pattern of the chord. This is based on its type.
	 * 
	 * @see ChordType
	 */
	private int[] pattern;

	/**
	 * 
	 * @param tonic
	 *            the tonic of the chord.
	 * @param type
	 *            the classification of the chord.
	 */
	public Chord(Note tonic, ChordType type) {
		// Get the MIDI note for the tonic.
		this.tonic = tonic.getMidiNote();
		// Set the pattern for the chord.
		pattern = type.getPattern();
	}

	/**
	 * Returns the MIDI note at the given chord degree and octave.
	 * 
	 * @param chordDegree
	 *            the degree of the new note in this chord. If chordDegree = 2,
	 *            this would mean the third note in the chord, 0 would mean the
	 *            first.
	 * @param octave
	 *            the octave of the chord.
	 * @return the MIDI note at the given chord degree and octave.
	 */
	public byte getMidiNote(int chordDegree, int octave) {
		// The MIDI note.
		int note = tonic;
		// The direction of the chord degree. 1 if it is positive, -1 if it is
		// negative.
		int direction = 1;
		// A number which is added to the result in order to wrap a negative
		// index.
		int indexWrapper = 0;

		// If the chord degree is negative.
		if (chordDegree < 0) {
			// Take the absolute instead.
			chordDegree *= -1;
			// Switch the direction.
			direction = -1;
			// Use the length of the chord pattern to wrap the index.
			indexWrapper = getChordPattern().length - 1;
		}

		// For each number starting from 0 to the chord degree.
		for (int intervalIndex = 0; intervalIndex < chordDegree; intervalIndex++) {
			// Wrap that number and get the next interval in the chord pattern.
			// Add this interval to the final note. If the direction is
			// negative, subtract it instead.
			note += (getChordPattern()[((intervalIndex * direction) % getChordPattern().length)
					+ indexWrapper])
					* direction;
		}

		// Transpose the note to its octave.
		note += octave * NOTES_IN_AN_OCTAVE;

		// If the note is negative, transpose it up a number of octaves to cover
		// the difference.
		int diff = 0;
		
		if (note < 0) {
			diff = note * -1;
		}

		// If the note is over the limit, transpose it down a number of octaves
		// to cover the difference.
		if (note > MAX_MIDI) {
			diff = MAX_MIDI - note;
		}

		note += (int) Math.ceil((float) Math.abs(diff) / NOTES_IN_AN_OCTAVE)
				* NOTES_IN_AN_OCTAVE * Math.signum(diff);

		// Return the final note.
		return (byte) note;
	}

	/**
	 * Return the interval pattern for this chord.
	 * 
	 * @see ChordType
	 * @return the interval pattern for this chord.
	 */
	public int[] getChordPattern() {
		return pattern;
	}
}
