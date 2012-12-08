package eecs598.experiments.analyzer;

public class ParameterBeliefPair implements Comparable<ParameterBeliefPair> {
	public double parameter;
	public double belief;
	
	public ParameterBeliefPair(double parameter, double belief) {
		super();
		this.parameter = parameter;
		this.belief = belief;
	}
	
	@Override
	public int compareTo(ParameterBeliefPair arg0) {
		return Double.compare(this.parameter, arg0.parameter);
	}

	@Override
	public String toString() {
		return "ParameterBelief [parameter=" + parameter + ", belief=" + belief
				+ "]";
	}
}
