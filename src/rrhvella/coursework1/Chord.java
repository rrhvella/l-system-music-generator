package rrhvella.coursework1;

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
