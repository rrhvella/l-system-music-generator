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