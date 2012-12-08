package eecs598.experiments;

import java.util.ArrayList;
import java.util.List;

import eecs598.experiments.analyzer.AverageBeliefTracker;
import eecs598.probability.Distribution;

public class OnlyNonBayesian extends RatioNonBayesianDeGroot {
	
	
	
	public OnlyNonBayesian(List<Distribution> possibleDistributions) {
		super(1.0, possibleDistributions, null);
	}

	public static void main(String[] args) {
		//runSanityCheck();
		//run2GaussiansEasy();
		run2GaussiansHard();
	}
	
	
	public static void runSanityCheck() {
		int uniformId = 42;
		System.out.println("SanityCheck: Zero and Uniform, Uniform ID = " + uniformId);
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getZeroAndUniform(uniformId, possibleDistributions);
		OnlyNonBayesian onlyNb = new OnlyNonBayesian(possibleDistributions);
		
		onlyNb.runErdosRenyi(new ExperimentController(trueDistribution), 10, 0.2);
	}
	
	public static void run2GaussiansEasy() {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getTwoGaussiansEasy(possibleDistributions);
		System.out.println("Two Gaussians Easy: True mean = " + trueDistribution.getParameter());
		
		OnlyNonBayesian onlyNb = new OnlyNonBayesian(possibleDistributions);
		
		onlyNb.runErdosRenyi(new ExperimentController(trueDistribution), 10, 0.2);
	}
	
	public static void run2GaussiansHard() {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getTwoGaussiansHard(possibleDistributions);
		System.out.println("Two Gaussians Hard: True mean = " + trueDistribution.getParameter());
		
		OnlyNonBayesian onlyNb = new OnlyNonBayesian(possibleDistributions);
		
		ExperimentController controller = new ExperimentController(trueDistribution);
		controller.attachAnalyzer(new AverageBeliefTracker(System.out));
		
		onlyNb.runErdosRenyi(controller, 100, 0.2);
	}
}
