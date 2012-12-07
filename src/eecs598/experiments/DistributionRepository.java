package eecs598.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eecs598.probability.Distribution;
import eecs598.probability.Gaussian;
import eecs598.test.helpers.TestDistributions;

public class DistributionRepository {
	/**
	 * Returns one "distribution" whose pdf is 0 everywhere and a uniform distribution
	 * whose "parameter" is the method's argument. Used as a sanity check -- all NB nodes
	 * should converge to the uniform distribution in one timestep. 
	 * @return the true distribution (i.e. the uniform)
	 */
	public static Distribution getZeroAndUniform(int uniformDistroId, List<Distribution> possibleDistributions) {
		if(uniformDistroId == 0) {
			throw new IllegalArgumentException("The ID for the uniform distribution cannot be 0.");
		}
		Distribution zero = new TestDistributions.ZeroPdf(0);
		Distribution uniform = new TestDistributions.Uniform(uniformDistroId);
		
		possibleDistributions.add(zero);
		possibleDistributions.add(uniform);
		
		return uniform;
	}
	
	/**
	 * Creates two Gaussians really far apart.
	 * @param possibleDistributions
	 * @return
	 */
	public static Distribution getTwoGaussiansEasy(List<Distribution> possibleDistributions) {
		throwIfNotEmpty(possibleDistributions);

		possibleDistributions.add(new Gaussian(2.0));
		possibleDistributions.add(new Gaussian(20.0));	
		
		return chooseRandom(possibleDistributions);
	}
	
	/**
	 * Creates two Gaussians close to each other.
	 * @param possibleDistributions
	 * @return
	 */
	public static Distribution getTwoGaussiansHard(List<Distribution> possibleDistributions) {
		throwIfNotEmpty(possibleDistributions);

		possibleDistributions.add(new Gaussian(10.0));
		possibleDistributions.add(new Gaussian(9.0));	
		
		return chooseRandom(possibleDistributions);
	}
	
	private static void throwIfNotEmpty(List<Distribution> list) {
		if(!list.isEmpty()) {
			throw new IllegalArgumentException("List of possible distributions must be empty.");
		}
	}
	
	private static Distribution chooseRandom(List<Distribution> list) {
		Random rnd = new Random();
		return list.get(rnd.nextInt(list.size()));
	}
}
