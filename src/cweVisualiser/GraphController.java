package cweVisualiser;
/*
 * Control what graph / data is being displayed to the user
 * 
 * Author: Conor Devilly
 * Date: 20151120
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import processing.core.*;

public class GraphController {
	/*
	 * We maintain a list of already existing graphs so
	 * so we do not have to keep calculating their positions over and over again
	 * and also to reduce queries to the database
	 */
	HashMap<Integer, WordCloud> wordClouds = new HashMap<Integer, WordCloud>(); //List of existing Word Clouds
	HashMap<String, LineGraph> lineGraphs = new HashMap<String, LineGraph>(); //List of existing Line Graphs
	HashMap<Integer, PieChart> pieCharts = new HashMap<Integer, PieChart>(); //List of existing Pie charts

	PApplet p;
	SQL db;
	Border border;
	Axis axis;
	int maxVal;

	//Constructor
	GraphController(PApplet p, SQL db, Border border){
		this.p = p;
		this.db = db;
		this.border = border;
		maxVal = 0;
		axis = new Axis(p, border, null, 0, 0);
	}

	//Removes a line graph so it will not be drawn
	void removeLineGraph(String id){
		LineGraph l = lineGraphs.get(id);
		lineGraphs.remove(id);

		/*
		 * If the line graph that was removed contained the maximum value in the
		 * selected data, we need to recalculate
		 * the axis values and also recalculate the points for each graph
		 * as they scale in accordance with the axis
		 */
		if(l.getMaxVal() == maxVal){
			maxVal = 0;
			for(Entry<String, LineGraph> g : lineGraphs.entrySet()){
				maxVal = (g.getValue().getMaxVal() > maxVal) ? g.getValue().getMaxVal() : maxVal;
			}
			axis = new Axis(p, border, l.data.keySet(), maxVal, l.data.size() - 1);
			for(Entry<String, LineGraph> g : lineGraphs.entrySet()){
				g.getValue().calcLineGraph(border, axis.getMaxVal());
			}
		}
	}

	//Add a line graph
	void addLineGraph(String id){
		LinkedHashMap<String, Integer> data = db.getCWERecsByID(id);
		int colour = p.color(p.random(0, 255), 127, p.random(0, 255));
		LineGraph l = new LineGraph(p, id, data, colour);

		/*
		 * If the line graph that was removed contained the maximum value in the
		 * selected data, we need to recalculate
		 * the axis values and also recalculate the points for each graph
		 * as they scale in accordance with the axis
		 */
		int newMax = l.getMaxVal();
		if(newMax > maxVal){
			maxVal = newMax;
			axis = new Axis(p, border, l.data.keySet(), maxVal, l.data.size() - 1);
			for(Entry<String, LineGraph> g : lineGraphs.entrySet()){
				g.getValue().calcLineGraph(border, axis.getMaxVal());
			}
		}
		l.calcLineGraph(border, axis.getMaxVal());
		lineGraphs.put(id, l);
	}

	//Draw each line graph
	void drawLineGraphs(){
		axis.drawAxis();
		/*
		 * If the mouse is over a line graph,
		 * that graph is "selected" and information
		 * about the graph will be displayed at the bottom of the screen
		 */
		String selected = null;

		//Draw every line graph in the list
		for(Entry<String, LineGraph> g : lineGraphs.entrySet()){
			//Check if a graph has been selected yet
			if(selected == null){
				for(int i = 1; i < g.getValue().coords.size(); i++){
					PVector from = g.getValue().coords.get(i - 1);
					PVector to = g.getValue().coords.get(i);
					if(mouseOver(from, to)){
						selected = g.getValue().id;
					}
				}
			}
			p.pushStyle();
			//If a graph is selected, we change its strokeWeight
			if(selected != null && selected.equals(g.getValue().id)){
				p.stroke(g.getValue().colour);
				p.strokeWeight(3);
				p.fill(g.getValue().colour);
				displayInfo(selected);
			}else{
				p.stroke(g.getValue().colour);
				p.strokeWeight(1);
			}
			//Draw the graph
			g.getValue().drawLineGraph(border, maxVal);
			p.popStyle();
		}
	}

	//Detect if the mouse is between any two pvectors
	boolean mouseOver(PVector from, PVector to){
		//This method is very maths heavy. I've tried to explain what's going on as best as I could
		int sensitivity = 2; //Change how far the mouse can be from the line before it is selected 
		PVector mouse = new PVector(p.mouseX, p.mouseY); //The mouse
		PVector Lock = new PVector(); //A point which "locks" to line between the to & from vectors
		PVector fromMouse = PVector.sub(mouse, from); //Difference between previously drawn point & the mouse
		PVector toFrom = PVector.sub(to, from); //Difference between previously drawn point & next point to be drawn

		/*
		 * The dot product of the to and from vectors
		 * This is essentially what you get if you
		 * multiply the scalar parts of both vectors (with respect to the angle between them).
		 * I.e: dot(a, b) = |a| * |b| cos(angleBetweenThem)
		 */
		float dotProduct = PVector.dot(toFrom, fromMouse);

		/* 
		 * Divide the dot by the magnitute of the line squared so as it returns the
		 * distance of the mouse from "From"
		 * as opposed the distance of the mouse from "to" - "from"
		 */
		dotProduct /= (toFrom.mag() * toFrom.mag());

		//Set the lock to the line between "to" and "from"
		Lock.set(toFrom.x, toFrom.y, 0);
		Lock.mult(dotProduct);
		Lock.add(from);	

		/* 
		 * If the mouse is on the line return true, else return false.
		 * To calculate if the mouse is on the line, we check if the dot product is between 0 and 1,
		 * this will tell us if the lock is on the line.
		 * We then check how far the mouse is from the lock. 
		 */
		if (dotProduct>0 && dotProduct<1 && mouse.dist(Lock) < sensitivity) return true;
		else return false;
	}

	//Displays information about a given CWE 
	void displayInfo(String id){
		//Query db for info about the id
		ArrayList<String> info = db.getCWEDescById(id);
		LinkedHashMap<String, Integer> data = db.getCWERecsByID(id);
		float x = border.left - 25;
		float y = p.height - border.bottom + 100;

		//Print information
		p.textAlign(PApplet.LEFT, PApplet.CENTER);
		p.textSize(14);
		p.text("ID: " + info.get(0), x, y);
		p.text("Name: "+ info.get(1), x, y + p.textAscent() + p.textDescent());
		p.text("Description: " + info.get(2), x, y + (p.textAscent() + p.textDescent()), 900, 100);

		x += 225;
		p.text("Year: ", x, y);
		p.text("Count: ", x, y + p.textAscent() + p.textDescent());

		//Print the number reported every year
		for(Entry<String, Integer> e : data.entrySet()){
			x += 50;
			p.text(e.getKey(), x, y);
			p.text(e.getValue(), x, y + p.textDescent() + p.textAscent());
		}
	}

	//Draws the word cloud
	void drawWordCloud(int year){
		p.background(0);
		WordCloud wc;
		//Check that the word cloud already exists, if not, create it
		if(wordClouds.containsKey(year)){
			wc = wordClouds.get(year);
		}else{
			LinkedHashMap<String, Integer> data = db.getCWERecsByYear(year);
			wc = new WordCloud(p, data);
			wc.calcWordCloud();
			wordClouds.put(year, wc);
		}
		wc.drawWordCloud();
	}

	//Draws a Pie Chart
	void drawPieChart(int year){
		p.background(0);
		PieChart pc;
		//Check that the word cloud already exists, if not, create it
		if(pieCharts.containsKey(year)){
			pc = pieCharts.get(year);
		}else{
			LinkedHashMap<String, Integer> data = db.getCWERecsByYear(year);
			pc = new PieChart(p, data);
			pieCharts.put(year, pc);
		}
		pc.drawPieChart();
	}
}
