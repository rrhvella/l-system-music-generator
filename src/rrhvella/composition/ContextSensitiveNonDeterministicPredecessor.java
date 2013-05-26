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
 * A predecessor of a production in a context-sensitive stochastic L-system.
 */
public class ContextSensitiveNonDeterministicPredecessor {
	/**
	 * The context which precedes the predecessor's letter.
	 */
	private String precedingContext;
	/**
	 * The context which follows the predecessor's letter.
	 */
	private String proceedingContext;
	/**
	 * Returns the probability that the production mapped to this predecessor
	 * will occur.
	 */
	private int probability;
	/**
	 * The entire context of the predecessor, including the letter.
	 */
	private String context;
	/**
	 * The length of the entire context.
	 */
	private int contextLength;

	/**
	 * Returns the probability that the production mapped to this predecessor
	 * will occur.
	 * 
	 * @return the probability that the production mapped to this predecessor
	 *         will occur.
	 */
	public int getProbability() {
		return probability;
	}

	/**
	 * 
	 * @param precedingContext
	 *            the context which precedes the predecessor's letter.
	 * @param letter
	 *            the letter of the predecessor.
	 * @param proceedingContext
	 *            the context which follows the predecessor's letter.
	 * @param probability
	 *            the probability that the production associated with this
	 *            predecessor will occur.
	 */
	public ContextSensitiveNonDeterministicPredecessor(String precedingContext,
			char letter, String proceedingContext, int probability) {
		this.precedingContext = precedingContext;
		this.proceedingContext = proceedingContext;
		this.probability = probability;

		// Store the context of this predecessor, as well as its length.
		context = precedingContext + letter + proceedingContext;
		contextLength = context.length();
	}

	/**
	 * 
	 * @param precedingContext
	 *            the context which precedes the predecessor's letter.
	 * @param letter
	 *            the letter of the predecessor.
	 * @param probability
	 *            the probability that the production associated with this
	 *            predecessor will occur.
	 */
	public ContextSensitiveNonDeterministicPredecessor(
			String preceedingContext, char letter, int probability) {
		this(preceedingContext, letter, "", probability);
	}

	/**
	 * 
	 * @param letter
	 *            the letter of the predecessor.
	 * @param probability
	 *            the probability that the production associated with this
	 *            predecessor will occur.
	 */
	public ContextSensitiveNonDeterministicPredecessor(char letter,
			int probability) {
		this("", letter, "", probability);
	}

	/**
	 * Returns true if the stream of characters starting from charIndex equal
	 * the context of this predecessor.
	 * 
	 * @param source
	 *            the string which is being checked.
	 * @param charIndex
	 *            the index which is mapped to the beginning of the context.
	 * @return true if the stream of characters starting from charIndex equal
	 *         the context of this predecessor.
	 */
	public boolean isContextValid(String source, int charIndex) {
		// If the context, starting from charIndex, is longer than the source
		// string, then return false.
		if (charIndex + contextLength > source.length()) {
			return false;
		}

		// For every character in the context of the predecessor.
		for (int contextCharIndex = 0; contextCharIndex < contextLength; contextCharIndex++) {
			// If, at the same position starting from charIndex, this character
			// does not have an equivalent character in the source string,
			// return false.
			if (source.charAt(contextCharIndex + charIndex) != context
					.charAt(contextCharIndex)) {
				return false;
			}
		}

		// If no inconsistencies have been found, return true.
		return true;
	}

	/**
	 * Returns the length of the predecessor's context.
	 * 
	 * @return the length of the predecessor's context.
	 */
	public int getContextLength() {
		return contextLength;
	}

	/**
	 * Returns the context which precedes the predecessor's letter.
	 * 
	 * @return the context which precedes the predecessor's letter.
	 */
	public String getPrecedingContext() {
		return precedingContext;
	}

	/**
	 * Returns the context which follows the predecessor's letter.
	 * 
	 * @return the context which follows the predecessor's letter.
	 */
	public String getProceedingContext() {
		return proceedingContext;
	}
}