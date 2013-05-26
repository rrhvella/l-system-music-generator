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

import java.util.HashMap;
import java.util.Stack;

/**
 * Generates a melody using a context-sensitive stochastic L-system.
 */
public class MelodyGenerator extends ContextSensitiveNonDeterministicLSystem {
	/**
	 * The productions for this grammar.
	 */
	private static final HashMap<ContextSensitiveNonDeterministicPredecessor, String> MELODY_PRODUCTIONS = getMelodyProductions();
	/**
	 * The maximum number of failed iterations this system can attempt before
	 * returning the original string.
	 * 
	 * @see MelodyGenerator#process(String)
	 */
	private static final int MAX_ITERATIONS = 20;

	/**
	 * 
	 * @param length
	 *            the number of bars in the melody.
	 */
	public MelodyGenerator(int length) {
		super(getAxiom(length), MELODY_PRODUCTIONS, true);
	}

	/**
	 * Returns the axiom needed to generate a melody of the given length.
	 * 
	 * According to the specification for this grammar, the notes are initially
	 * very long, and the productions will then subdivide them into smaller ones
	 * (Worth & Stepney, 2005a, p. 5). In this application, the initial long
	 * notes occupy a single bar. Therefore, the axiom returned should be a
	 * number of consecutive Fs equal to the length. For example, a length of 3
	 * will give "FFF" and a length of 6 will give "FFFFFF".
	 * 
	 * @param length
	 *            the length of the melody, in bars.
	 * @return the axiom to generate a melody of the given length.
	 */
	private static String getAxiom(int length) {
		// Generate the axiom by concatenating a number of Fs equal to the
		// length.
		String axiom = "F";

		for (int noteIndex = 1; noteIndex < length; noteIndex++) {
			axiom += "F";
		}

		// Return the axiom.
		return axiom;
	}

	/**
	 * Process the given string according to the rewriting the rules of this
	 * grammar and return the result.
	 * 
	 * @param source
	 *            the string which will be rewritten.
	 * @return the result of the rewriting process.
	 */
	public String process(String source) {
		String result = "";

		// Keep iterating until a valid descendant of this string is found.
		// After a number of failed iterations, the original string should be
		// returned instead.
		int iterations = 0;

		while (!valid(result = super.process(source))) {
			if (++iterations == MAX_ITERATIONS) {
				return source;
			}
		}

		return result;
	}

	/**
	 * Returns true if the given string is valid.
	 * 
	 * A string is valid if and only if, during rendering no attempt is made to
	 * divide a note equal to 1 PPQ. If such an attempt is made during the
	 * actual rendering process, an error would be caused.
	 * 
	 * @param stringToCheck
	 *            the string which will be validated.
	 * @return true if the given string is valid.
	 */
	private boolean valid(String stringToCheck) {
		// The length, in PPQ, of a single bar.
		int length = StructuredPolyphonicLSystemNotator.BAR_LENGTH;

		// The state stack for the duration of the note.
		Stack<Integer> lengthStack = new Stack<Integer>();

		// For each character in the given string.
		for (char characterToken : stringToCheck.toCharArray()) {
			// Interpret the character, and perform the operations which
			// directly effect the duration of the note.
			switch (characterToken) {
			case 'd':
				// If an attempt is made to divide a note which is equal to 1
				// PPQ, return false.
				if (length == 1) {
					return false;
				}

				// Halve the note duration.
				length /= 2;
				break;
			case 'D':
				// Double the note duration.
				length *= 2;
				break;
			case '[':
				// Push the current duration onto the stack.
				lengthStack.push(length);
				break;
			case ']':
				// Pop the previous duration from the stack.
				length = lengthStack.pop();
				break;
			}
		}

		// If no anomalies were found, return true.
		return true;
	}

	/**
	 * Returns the productions of the melody generator.
	 * 
	 * The productions are a direct translations of the ones used by Worth and
	 * Stepney for their 'Random Branching' grammar (2005b, "Random branching").
	 * 
	 * @return the productions of the melody generator.
	 */
	private static HashMap<ContextSensitiveNonDeterministicPredecessor, String> getMelodyProductions() {

		HashMap<ContextSensitiveNonDeterministicPredecessor, String> productions = new HashMap<ContextSensitiveNonDeterministicPredecessor, String>();
		productions.put(
				new ContextSensitiveNonDeterministicPredecessor('F', 26), "F");
		productions
				.put(new ContextSensitiveNonDeterministicPredecessor('F', 1),
						"dFFD");
		productions.put(
				new ContextSensitiveNonDeterministicPredecessor('F', 1),
				"[dFF]");
		productions.put(
				new ContextSensitiveNonDeterministicPredecessor('F', 1),
				"d-F+FD");
		productions.put(
				new ContextSensitiveNonDeterministicPredecessor('F', 1),
				"[d-F+F]");
		productions.put(
				new ContextSensitiveNonDeterministicPredecessor('F', 1),
				"d+F-FD");
		productions.put(
				new ContextSensitiveNonDeterministicPredecessor('F', 1),
				"[d+F-F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F",
				'F', 1), "Fd+F-FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F",
				'F', 1), "[Fd+F-F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F",
				'F', 1), "Fd-F+FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F",
				'F', 1), "[Fd-F+F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F+",
				'F', 1), "Fd++F-FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F+",
				'F', 1), "[Fd++F-F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F+",
				'F', 1), "Fd+++F--FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F+",
				'F', 1), "[Fd+++F--F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F+",
				'F', 1), "Fd-F++FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F+",
				'F', 1), "[Fd-F++F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F+",
				'F', 1), "Fd--F+++FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F+",
				'F', 1), "[Fd--F+++F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F-",
				'F', 1), "-Fd++F-FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F-",
				'F', 1), "[-Fd++F-F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F-",
				'F', 1), "-Fd+++F--FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F-",
				'F', 1), "[-Fd+++F--F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F-",
				'F', 1), "-Fd-F++FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F-",
				'F', 1), "[-Fd-F++F]");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F-",
				'F', 1), "-Fd--F+++FD");
		productions.put(new ContextSensitiveNonDeterministicPredecessor("F-",
				'F', 1), "[-Fd--F+++F]");

		return productions;
	}
}