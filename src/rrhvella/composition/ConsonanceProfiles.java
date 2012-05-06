package rrhvella.composition;

/**
 * For each scale, ConsonanceProfiles associates an interval from the tonic with a
 * consonance factor. This indicates how consonant the note, at that interval
 * from the tonic, is.
 * 
 * The profiles specified in this class are based on a mixture of western
 * classical music theory and the author's own tastes.
 */
public class ConsonanceProfiles {
	/**
	 * The consonance profile for a major scale.
	 */
	public static final int[] major = new int[] { 4, -3, 1, -3, 1, 2, -3, 4,
			-3, 1, 1, -3 };
	/**
	 * The consonance profile for a minor scale.
	 */
	public static final int[] minor = new int[] { 4, -3, 1, 2, -3, 2, -3, 4, 1,
			-3, 2, -3 };
}