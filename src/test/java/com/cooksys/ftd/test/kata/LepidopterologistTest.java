package com.cooksys.ftd.test.kata;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cooksys.ftd.kata.ILepidopterologist;
import com.cooksys.ftd.kata.impl.Lepidopterologist;
import com.cooksys.ftd.kata.model.Butterpillar;
import com.cooksys.ftd.kata.model.Catterfly;
import com.cooksys.ftd.kata.model.GrowthModel;
import com.cooksys.ftd.kata.model.Sample;
import com.cooksys.ftd.kata.model.Species;

public class LepidopterologistTest {
	
	ILepidopterologist lepMan;
	GrowthModel[] gList;
	Species[] speciesList;
	Sample[] sampleList;
	
	@Before
	public void before() {
		lepMan = new Lepidopterologist();
		
		gList = new GrowthModel[5];
		gList[0] = new GrowthModel(1.2, 0.5);
		gList[1] = new GrowthModel(1.5, 0.4);
		gList[2] = new GrowthModel(0.9, 1.1);
		gList[3] = new GrowthModel(1.1, 0.9);
		gList[4] = new GrowthModel(1.0, 1.0);
		
		speciesList = new Species[5];
		speciesList[0] = new Species("A", gList[0], 0.001);	// 1
		speciesList[1] = new Species("B", gList[1], 0.001);	// 2
		speciesList[2] = new Species("E", gList[2], 0.001);	// 5
		speciesList[3] = new Species("D", gList[3], 0.001);	// 4
		speciesList[4] = new Species("C", gList[4], 0.001);	// 3
		
		sampleList = new Sample[8];
		Butterpillar b0 = new Butterpillar(1.4, 12.0);
		Catterfly c0 = gList[0].convert(b0);
		sampleList[0] = new Sample(b0, c0);
		
		Butterpillar b1 = new Butterpillar(1.5, 24.0);
		Catterfly c1 = gList[1].convert(b1);
		sampleList[1] = new Sample(b1, c1);
		
		Butterpillar b2 = new Butterpillar(1.1, 18.0);
		Catterfly c2 = gList[2].convert(b2);
		sampleList[2] = new Sample(b2, c2);
		
		Butterpillar b3 = new Butterpillar(1.0, 22.0);
		Catterfly c3 = gList[3].convert(b3);
		sampleList[3] = new Sample(b3, c3);
		
		Butterpillar b4 = new Butterpillar(2.0, 3.0);
		Catterfly c4 = gList[4].convert(b4);
		sampleList[4] = new Sample(b4, c4);
		
		Butterpillar b5 = new Butterpillar(1.9, 32.0);
		Catterfly c5 = gList[1].convert(b5);
		sampleList[5] = new Sample(b5, c5);
		
		Butterpillar b6 = new Butterpillar(1.2, 26.0);
		Catterfly c6 = gList[1].convert(b6);
		sampleList[6] = new Sample(b6, c6);
		
		Butterpillar b7 = new Butterpillar(1.0, 37.0);
		Catterfly c7 = gList[1].convert(b7);
		sampleList[7] = new Sample(b7, c7);
	}
	
	@After
	public void after() {
		lepMan = null;
		
		gList = null;
		speciesList = null;
		sampleList = null;
	}
	
	@Test
	public void testRegisterSpecies() {
		assertTrue(lepMan.registerSpecies(speciesList[0]));
		assertFalse(lepMan.registerSpecies(speciesList[0]));
		assertTrue(lepMan.registerSpecies(speciesList[1]));
		assertTrue(lepMan.registerSpecies(speciesList[2]));
		assertTrue(lepMan.registerSpecies(speciesList[3]));
		assertTrue(lepMan.registerSpecies(speciesList[4]));
		assertFalse(lepMan.registerSpecies(speciesList[4]));
	}

	@Test
	public void testIsSpeciesRegistered() {
		lepMan.registerSpecies(speciesList[0]);
		assertTrue(lepMan.isSpeciesRegistered(speciesList[0]));
		assertFalse(lepMan.isSpeciesRegistered(speciesList[1]));
		lepMan.registerSpecies(speciesList[1]);
		assertTrue(lepMan.isSpeciesRegistered(speciesList[1]));
	}

	@Test
	public void testFindSpeciesForSample() {
		lepMan.registerSpecies(speciesList[0]);
		lepMan.registerSpecies(speciesList[1]);
		lepMan.registerSpecies(speciesList[2]);
		lepMan.registerSpecies(speciesList[3]);
		lepMan.recordSample(sampleList[2]);
		Optional<Species> compare = lepMan.findSpeciesForSample(sampleList[2]);
		assertTrue(compare.isPresent());
		
		Optional<Species> compare2 = lepMan.findSpeciesForSample(sampleList[4]);
		assertFalse(compare2.isPresent());
	}

	@Test
	public void testRecordSample() {
		lepMan.registerSpecies(speciesList[0]);
		lepMan.registerSpecies(speciesList[1]);
		assertTrue(lepMan.recordSample(sampleList[0]));
		assertFalse(lepMan.recordSample(sampleList[2]));
		lepMan.registerSpecies(speciesList[2]);
		assertTrue(lepMan.recordSample(sampleList[2]));
	}

	@Test
	public void testGetSamplesForSpecies() {
		lepMan.registerSpecies(speciesList[1]);
		lepMan.recordSample(sampleList[1]);
		lepMan.recordSample(sampleList[5]);
		
		List<Sample> compareList = lepMan.getSamplesForSpecies(speciesList[1]);
		assertTrue(compareList.get(0).equals(sampleList[1]));
		assertTrue(compareList.get(1).equals(sampleList[5]));
	}

	@Test
	public void testGetRegisteredSpecies() {
		List<Species> sortedList = new ArrayList<>(Arrays.asList(speciesList));
		Collections.sort(sortedList);
		
		lepMan.registerSpecies(speciesList[0]);
		lepMan.registerSpecies(speciesList[1]);
		lepMan.registerSpecies(speciesList[2]);
		lepMan.registerSpecies(speciesList[3]);
		lepMan.registerSpecies(speciesList[4]);
		
		List<Species> compare = lepMan.getRegisteredSpecies();
		
		for (int i = 0; i < 5; i++) {
			assertTrue(sortedList.get(i).equals(compare.get(i)));
		}
		
		assertTrue(compare.get(0).equals(speciesList[0]));
		assertTrue(compare.get(1).equals(speciesList[1]));
		assertFalse(compare.get(2).equals(speciesList[2]));
		assertTrue(compare.get(2).equals(speciesList[4]));
	}

	@Test
	public void testGetTaxonomy() {
		Map<Species, List<Sample>> sortedMap = new TreeMap<Species, List<Sample>>();
		
		List<Sample> speciesAList = new ArrayList<>();
		speciesAList.add(sampleList[0]);
		
		List<Sample> speciesBList = new ArrayList<>();
		speciesBList.add(sampleList[1]);
		speciesBList.add(sampleList[5]);
		speciesBList.add(sampleList[6]);
		speciesBList.add(sampleList[7]);
		Collections.sort(speciesBList);
		
		List<Sample> speciesCList = new ArrayList<>();
		speciesCList.add(sampleList[2]);
		
		List<Sample> speciesDList = new ArrayList<>();
		speciesDList.add(sampleList[3]);
		
		List<Sample> speciesEList = new ArrayList<>();
		speciesEList.add(sampleList[4]);
		
		sortedMap.put(speciesList[0], speciesAList);
		sortedMap.put(speciesList[1], speciesBList);
		sortedMap.put(speciesList[2], speciesCList);
		sortedMap.put(speciesList[3], speciesDList);
		sortedMap.put(speciesList[4], speciesEList);
		
		lepMan.registerSpecies(speciesList[0]);
		lepMan.registerSpecies(speciesList[1]);
		lepMan.registerSpecies(speciesList[2]);
		lepMan.registerSpecies(speciesList[3]);
		lepMan.registerSpecies(speciesList[4]);
		
		lepMan.recordSample(sampleList[0]);
		lepMan.recordSample(sampleList[1]);
		lepMan.recordSample(sampleList[2]);
		lepMan.recordSample(sampleList[3]);
		lepMan.recordSample(sampleList[4]);
		lepMan.recordSample(sampleList[5]);
		lepMan.recordSample(sampleList[6]);
		lepMan.recordSample(sampleList[7]);
		
		Map<Species, List<Sample>> compareMap = lepMan.getTaxonomy();
		
		Set<Species> sortedKeySet = sortedMap.keySet();
		Set<Species> compareKeySet = compareMap.keySet();
		
		List<Species> sortedKS = new ArrayList<>(sortedKeySet);
		List<Species> compareKS = new ArrayList<>(compareKeySet);
		for (int i = 0; i < sortedKeySet.size(); i++)
			assertTrue(sortedKS.get(i).equals(compareKS.get(i)));
		
		List<Sample> sortedAList = sortedMap.get(sortedKS.get(0));
		List<Sample> sortedBList = sortedMap.get(sortedKS.get(1));
		List<Sample> sortedCList = sortedMap.get(sortedKS.get(2));
		List<Sample> sortedDList = sortedMap.get(sortedKS.get(3));
		List<Sample> sortedEList = sortedMap.get(sortedKS.get(4));
		
		List<Sample> compareAList = compareMap.get(compareKS.get(0));
		List<Sample> compareBList = compareMap.get(compareKS.get(1));
		List<Sample> compareCList = compareMap.get(compareKS.get(2));
		List<Sample> compareDList = compareMap.get(compareKS.get(3));
		List<Sample> compareEList = compareMap.get(compareKS.get(4));
		
		for (int i = 0; i < compareAList.size(); i++)
			assertTrue(compareAList.get(i).equals(sortedAList.get(i)));
		
		for (int i = 0; i < compareBList.size(); i++) 
			assertTrue(compareBList.get(i).equals(sortedBList.get(i)));
		
		for (int i = 0; i < compareCList.size(); i++)
			assertTrue(compareCList.get(i).equals(sortedCList.get(i)));
		
		for (int i = 0; i < compareDList.size(); i++)
			assertTrue(compareDList.get(i).equals(sortedDList.get(i)));
		
		for (int i = 0; i < compareEList.size(); i++)
			assertTrue(compareEList.get(i).equals(sortedEList.get(i)));
	}

}
