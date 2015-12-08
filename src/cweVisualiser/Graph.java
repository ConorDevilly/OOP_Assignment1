package cweVisualiser;
/*
 * General Graph class
 * 
 * Author: Conor Devilly
 * Date: 20151109
 */
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import processing.core.PApplet;

public class Graph {
	//Fields
	PApplet p;
	LinkedHashMap<String, Integer> data;
	
	Graph(PApplet p, LinkedHashMap<String, Integer> data){
		this.data = data;
		this.p = p;
	}
	
	//Return the maximum value of the data
	int getMaxVal(){
		int max = 0;

		//Loop through each entry, if the entry's value is greater than the current max, set the current max to it
		for(Entry<String, Integer> e : data.entrySet()){
			max = (e.getValue() > max) ? e.getValue() : max;
		}
		return max;
	}
}
