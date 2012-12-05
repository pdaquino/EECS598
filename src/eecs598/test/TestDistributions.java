package eecs598.test;

import eecs598.probability.Distribution;
import eecs598.util.NotImplementedException;

public class TestDistributions {
	public static class Uniform implements Distribution {
		private int id;
		
		public Uniform(int id) {
			super();
			this.id = id;
		}

		@Override
		public double draw() {
			throw new NotImplementedException();
		}

		@Override
		public double pdf(double x) {
			return 1;
		}

		@Override
		public double getParameter() {
			return id;
		}
	}
	
	public static class ZeroPdf implements Distribution {
		private int id;

		
		public ZeroPdf(int id) {
			super();
			this.id = id;
		}

		@Override
		public double draw() {
			throw new IllegalArgumentException();
		}

		@Override
		public double pdf(double x) {
			return 0;
		}

		@Override
		public double getParameter() {
			return id;
		}
		
	}
}
