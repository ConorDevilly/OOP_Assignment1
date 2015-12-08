package cweVisualiser;
/*
 * This class is a line graph
 * 
 * Author: Conor Devilly
 * Date: 20151111
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;

import processing.core.*;

public class LineGraph extends Graph{

	//Fields
	int colour;
	ArrayList<PVector> coords;
	ArrayList<PVector> oldCoords;
	float items;
	float tx= 0, ty = 0;
	String id;
	String drawn;
	//boolean drawn;

	//Constructor
	LineGraph(PApplet p, String id, LinkedHashMap<String, Integer> data, int colour) {
		super(p, data);
		this.colour = colour;
		this.id = id;
		items = data.size();
		coords = new ArrayList<PVector>();
		oldCoords = new ArrayList<PVector>();
		drawn = null;
	}

	//Calculates the points where the line graph should be
	void calcLineGraph(Border border, float max){
		//Copy previous coords to use in animation
		oldCoords = new ArrayList<PVector>();
		for(PVector p : coords){
			PVector copy = new PVector(p.x, p.y);
			oldCoords.add(copy);
		}
		//Reset the old coords
		coords = new ArrayList<PVector>();
		//Take a copy of the values inside the data hashmap as they cannot be accessed via indexes
		ArrayList<Integer> vals = new ArrayList<Integer>(data.values());

		for (int i = 1 ; i < items; i ++){
			float x1 = PApplet.map(i - 1, 0, items - 1, border.left, p.width - border.right);
			float x2 = PApplet.map(i, 0, items - 1, border.left, p.width - border.right);
			float y1 = PApplet.map(vals.get(i-1), 0, max, p.height - border.bottom, border.top);
			float y2 = PApplet.map(vals.get(i), 0, max, p.height - border.bottom, border.top);
			PVector pos1 = new PVector(x1, y1);
			PVector pos2 = new PVector(x2, y2);
			if(i == 1) coords.add(pos1); //Add the initial point
			coords.add(pos2);
		}
		//Set whether the graph is to be drawn or redrawn
		drawn = (oldCoords.isEmpty()) ? "NotDrawn" : "reDraw";
	}

	//Displays the line graph
	void drawLineGraph(Border border, float max){

		PVector pos1 = null;
		PVector pos2 = null;

		//We do different things based on whether the graph is being animated to a different position or not
		//Animations are extremely slow as I did not have time to write the code to calculate a good speed
		switch(drawn){
			//If the graph has just being added to the screen
			case "NotDrawn":
				//Set the drawn flag to drawn
				drawn = "Drawn";
				for(int i = 1; i < coords.size(); i ++){
					PVector from = new PVector(coords.get(i - 1).x, coords.get(i - 1).y);
					PVector to = new PVector(coords.get(i).x, coords.get(i).y);

					//First we add each coordinate another array
					if(oldCoords.size() <= i){
						oldCoords.add(from);
						drawn = "NotDrawn";
					}else{
						pos1 = oldCoords.get(i - 1);

						//Check if the x coord needs to be moved
						//else if used instead of else in case pos1.x == to.x
						if((int) pos1.x > (int) to.x) pos1.x--;
						else if((int) pos1.x < (int) to.x) pos1.x++;

						//Check if the y coords needs to be moved
						if((int) pos1.y > (int) to.y) pos1.y--;
						else if((int) pos1.y < (int) to.y) pos1.y++;

						//Draw the line
						p.line(from.x, from.y, pos1.x, pos1.y);

						//Check if the current position & the new position are roughly the same
						//If not, reset the drawn flag to redrawn
						if(!(PApplet.abs(pos1.x - to.x) <= 2 && PApplet.abs(pos1.y - to.y) <= 2)){
							drawn = "NotDrawn";
						}
					}
				}
				break;
			//If the graph simply has to be displayed
			case "Drawn":
				for(int i = 1; i < coords.size(); i ++){
					pos1 = coords.get(i - 1);
					pos2 = coords.get(i);
					p.line(pos1.x, pos1.y, pos2.x, pos2.y);
				}
				break;
			//If the graph is moving from one position to another (i.e. when the axis changes)
			case "reDraw":
				for(int i = 1; i < coords.size(); i ++){
					pos1 = oldCoords.get(i - 1);
					pos2 = oldCoords.get(i);
					PVector newP1 = coords.get(i - 1);
					PVector newP2 = coords.get(i);
					
					//Check if the points need to be moved
					if((int) pos1.y > newP1.y) pos1.y--; else pos1.y++;
					if((int) pos2.y > newP2.y) pos2.y--; else pos2.y++;

					p.line(pos1.x, pos1.y, pos2.x, pos2.y);

					//Check if the current position & the new position are roughly the same
					if(PApplet.abs(pos1.y - newP1.y) <= 2 && PApplet.abs(pos2.y - newP2.y) <= 2 && i == coords.size() - 1){
						drawn = "Drawn";
					}
				}
				break;
		}
	}
}