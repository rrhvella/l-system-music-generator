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