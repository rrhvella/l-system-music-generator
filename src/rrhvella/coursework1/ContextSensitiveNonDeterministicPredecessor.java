package rrhvella.coursework1;

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