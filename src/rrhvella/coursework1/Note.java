package rrhvella.coursework1;

/**
 * An abstract description of a frequency in Western music.
 */
public enum Note {
	C(0), CSHARP(1), DFLAT(1), D(2), DSHARP(3), EFLAT(3), E(4), F(5), FSHARP(6), GFLAT(
			6), G(7), GSHARP(8), AFLAT(8), A(9), ASHARP(10), BFLAT(10), B(11);

	/**
	 * The MIDI byte associated with this note. This is derived from the value
	 * of the first C, as specified on the MIDI Manufacturers' association
	 * website (2004).
	 */
	private byte midiNote;

	/**
	 * The order of the notes.
	 */
	private static Note[] noteOrdering = new Note[] { C, CSHARP, D, DSHARP, E,
			F, FSHARP, G, GSHARP, A, ASHARP, B };

	/**
	 * 
	 * @param midiNote
	 *            the first MIDI byte equivalent to this note.
	 */
	Note(int midiNote) {
		this((byte) midiNote);
	}

	/**
	 * 
	 * @param midiNote
	 *            the first MIDI byte equivalent to this note.
	 */
	Note(byte midiNote) {
		this.midiNote = midiNote;
	}

	/**
	 * Returns the first MIDI byte equivalent to this note.
	 * 
	 * @return the first MIDI byte equivalent to this note.
	 */
	public byte getMidiNote() {
		return midiNote;
	}

	/**
	 * Returns the note which has the given interval from the tonic.
	 * 
	 * @param tonic
	 *            the tonic from which the new note is derived.
	 * @param interval
	 *            the interval of the new note from the tonic.
	 * @return the new note.
	 */
	public static Note fromInterval(Note tonic, int interval) {
		// Generate the index of the new note by adding the interval to the
		// tonic and wrapping it around.
		int newNoteIndex = (tonic.midiNote + interval) % noteOrdering.length;

		// If the resulting index is negative, add the number of notes to it, so
		// that it can be wrapped around.
		if (newNoteIndex < 0) {
			newNoteIndex += noteOrdering.length;
		}

		// Return the new note based on the index.
		return noteOrdering[newNoteIndex];
	}
}