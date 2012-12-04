package eecs598.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import eecs598.probability.ProbabilityUtil;

public class ProbabilityUtilTest {

	@Test
	public void expectedValueTest() {
		HashMap<Double, Double> probMap = new HashMap<>();
		probMap.put(1.0, .5);
		probMap.put(2.0, .5);
		assertEquals(1.5, ProbabilityUtil.expectedValue(probMap), 0.0000001);
	}
	
	@Test
	public void expectedValueTest2() {
		HashMap<Double, Double> probMap = new HashMap<>();
		probMap.put(0.0, .5);
		probMap.put(0.0, .5);
		assertEquals(0, ProbabilityUtil.expectedValue(probMap), 0.0000001);
	}
	
	@Test
	public void expectedValueTest3() {
		HashMap<Double, Double> probMap = new HashMap<>();
		probMap.put(1.0, -.5);
		probMap.put(2.0, .5);
		try {
			ProbabilityUtil.expectedValue(probMap);
			fail();
		} catch(IllegalArgumentException e) {}
	}

	@Test
	public void expectedValueTest4() {
		HashMap<Double, Double> probMap = new HashMap<>();
		probMap.put(1.0, 1.5);
		probMap.put(2.0, .5);
		try {
			ProbabilityUtil.expectedValue(probMap);
			fail();
		} catch(IllegalArgumentException e) {}
	}

}
