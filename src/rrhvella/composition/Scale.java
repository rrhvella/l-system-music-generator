package rrhvella.composition;

import java.util.HashMap;

/**
 * The harmonic information associated with a classical Western scale.
 */
public enum Scale {
	MAJOR(ConsonanceProfiles.major, ChordProfiles.major), MINOR(
			ConsonanceProfiles.minor, ChordProfiles.minor);

	/**
	 * The consonance profile associated with this scale.
	 * 
	 * @see ConsonanceProfiles
	 */
	private int[] consonanceProfile;
	/**
	 * The chord profile associated with this scale.
	 * 
	 * @see ChordProfiles
	 */
	private char[] chordProfile;

	/**
	 * 
	 * @param consonanceProfile
	 *            the consonance profile associated with this scale.
	 * @param chordProfile
	 *            the chord profile associated with this scale.
	 */
	Scale(int[] consonanceProfile, char[] chordProfile) {
		this.consonanceProfile = consonanceProfile;
		this.chordProfile = chordProfile;
	}

	/**
	 * Returns the L-system which generates the chord progression for a key with
	 * this scale.
	 * 
	 * @return the L-system which generates the chord progression for a key with
	 *         this scale.
	 */
	public ContextSensitiveNonDeterministicLSystem getChordSystem() {
		// The productions for the new grammar.
		HashMap<ContextSensitiveNonDeterministicPredecessor, String> productions = new HashMap<ContextSensitiveNonDeterministicPredecessor, String>();

		// For each combination of two notes.
		for (int firstNoteIndex = 0; firstNoteIndex < consonanceProfile.length; firstNoteIndex++) {
			for (int secondNoteIndex = 0; secondNoteIndex < consonanceProfile.length; secondNoteIndex++) {
				// The interval between the second note and the first.
				int interval = secondNoteIndex - firstNoteIndex;

				// If the interval is less than 0, add the number of notes in an
				// Octave to wrap it around.
				if (interval < 0) {
					interval += Chord.NOTES_IN_AN_OCTAVE;
				}

				// Format the note numbers as two character strings, padding
				// them to the left with 0s.
				String firstNoteString = String.format("%02d",
						firstNoteIndex + 1);
				String secondNoteString = String.format("%02d",
						secondNoteIndex + 1);

				// Get the consonance profile for the first note.
				int[] firstNoteConsonanceProfile = ConsonanceProfiles.major;

				if (chordProfile[firstNoteIndex] == ChordProfiles.MINOR_TRIAD) {
					firstNoteConsonanceProfile = ConsonanceProfiles.minor;
				}

				// Get the consonance for the combination by adding the
				// consonance of the second note with the first, with the
				// consonance of the first note with the key's tonic.
				int consonance = firstNoteConsonanceProfile[interval]
						+ consonanceProfile[secondNoteIndex];

				// If the combination is dissonant, do not add it to the
				// productions.
				if (consonance < 1) {
					continue;
				}

				// Add the combination to the production rules for the grammar.
				//
				// The predecessor of the production should be a single chord,
				// which has the tonic equal to the first note in this
				// combination.
				//
				// The successor should be the chord for the first
				// note concatenated with the chord for the second note. The
				// chords should be a string with the interval of their tonic
				// from the key's tonic as the first two characters (01 - 12).
				// The third character should be either m or M, which represent
				// a minor triad and a major triad, respectively.
				//
				// For example, 01m represents a minor triad in the tonic of the
				// key. 08M represents a major triad in the dominant of the key.
				productions.put(
						new ContextSensitiveNonDeterministicPredecessor(
								firstNoteString, chordProfile[firstNoteIndex],
								consonance), firstNoteString
								+ chordProfile[firstNoteIndex]
								+ secondNoteString
								+ chordProfile[secondNoteIndex]);
			}
		}

		// Return the new grammar. Set the chord 01M as the axiom (or 01m if the
		// scale is
		// minor).
		return new ContextSensitiveNonDeterministicLSystem("01"
				+ chordProfile[0], productions, true);
	}

}