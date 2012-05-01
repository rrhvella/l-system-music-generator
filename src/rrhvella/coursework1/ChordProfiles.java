package rrhvella.coursework1;

/**
 * For each scale, ChordProfiles associates a chord for a note at a given
 * offset from the scale's tonic. For example, for the key of C Major,
 * ChordProfiles associates a major triad with the note G -- G being 7 semitones
 * away from the key's tonic, C. Currently, this class only specifies a major
 * or minor triad.
 * 
 * The profiles specified in this class are based on a mixture of western
 * classical music theory and the author's own tastes.
 */
public class ChordProfiles {
	/**
	 * The character associated with a minor triad.
	 */
	public static final char MINOR_TRIAD = 'm';
	/**
	 * The character associated with a major triad.
	 */
	public static final char MAJOR_TRIAD = 'M';

	/**
	 * The chord profile for a major scale.
	 */
	public static final char[] major = new char[] { MAJOR_TRIAD, MINOR_TRIAD,
			MINOR_TRIAD, MAJOR_TRIAD, MINOR_TRIAD, MAJOR_TRIAD, MINOR_TRIAD,
			MAJOR_TRIAD, MINOR_TRIAD, MINOR_TRIAD, MAJOR_TRIAD, MINOR_TRIAD };
	/**
	 * The chord profile for a minor scale.
	 */
	public static final char[] minor = new char[] { MINOR_TRIAD, MAJOR_TRIAD,
			MAJOR_TRIAD, MAJOR_TRIAD, MINOR_TRIAD, MINOR_TRIAD, MAJOR_TRIAD,
			MINOR_TRIAD, MAJOR_TRIAD, MINOR_TRIAD, MAJOR_TRIAD, MINOR_TRIAD };

	/**
	 * Returns the chord type associated with the given character. If an invalid
	 * character is given, ChordType.MINOR_TRIAD will be returned by default.
	 * 
	 * @param character
	 *            the character from which the chord type will be derived. The
	 *            constants associated with this class should be used (
	 *            {@link MINOR_TRIAD}, {@link MAJOR_TRIAD}).
	 * @return the chord type associated with the given character.
	 */
	public static ChordType chordTypeFromCharacter(char character) {
		switch (character) {
		case MAJOR_TRIAD:
			return ChordType.MAJOR_TRIAD;
		default:
			return ChordType.MINOR_TRIAD;
		}
	}
}
