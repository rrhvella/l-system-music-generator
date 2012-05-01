package rrhvella.coursework1;

/**
 * Specifies the classification for a chord, as well as the interval pattern
 * associated with that classification. Currently, ChordType only supports the
 * major and minor triads.
 * 
 * The interval pattern for the chord is stored as an array of integers. Each
 * integer specifies the interval between one note and the next. For example,
 * the major triad has the interval pattern: 4, 3, 5. This means that the second
 * note in the chord is 4 semitones away from the first; the third note is 3
 * semitones away from the second, and so on.
 * 
 * The interval patterns specified in this class are the same as those used in
 * western classical music.
 */
public enum ChordType {
	MAJOR_TRIAD(new int[] { 4, 3, 5 }), MINOR_TRIAD(new int[] { 3, 4, 5 });

	/**
	 * the pattern for the code.
	 */
	private int[] pattern;

	ChordType(int[] pattern) {
		this.pattern = pattern;
	}


	/**
	 * Returns the pattern for the chord.
	 * 
	 * @param pattern
	 *            the pattern for the chord.
	 */
	public int[] getPattern() {
		return pattern;
	}
}