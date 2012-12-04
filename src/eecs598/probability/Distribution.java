package eecs598.probability;

/**
 * This interface represents a distribution with one parameter.
 * @author Pedro
 *
 */
public interface Distribution {
	
	/**
	 * Draws a random sample from the distribution.
	 * @return
	 */
	public double draw();
	
	/**
	 * The pdf at the sample point x.
	 * @param x
	 */
	public double pdf(double x);
	
	/**
	 * The parameter of the underlying distribution.
	 * @return
	 */
	public double getParameter();
}
