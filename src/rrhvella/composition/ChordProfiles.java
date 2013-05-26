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
