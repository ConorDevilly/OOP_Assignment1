package cweVisualiser;
/*
 * This class draws an Axis based on what data is to be displayed
 * 
 * Author: Conor Devilly
 * Date: 20151109
 */
import processing.core.*;

import java.util.Iterator;
import java.util.Set;

public class Axis {
	//Fields
	PApplet p;
	Border border;
	Graph graph;
	Set<String> keys;
	int maxVal;
	int items;
	int measureUnit;

	//Constructor
	Axis(PApplet p, Border border, Set<String> keys, int maxVal, int items){
		this.p = p;
		this.border = border;
		this.keys = keys;
		this.maxVal = maxVal;
		this.items = items;
		measureUnit = calcMeasureUnit();
	}

	//Draw the axis
	void drawAxis(){
		int horizontalIntervals = items;
		int verticalIntervals = (int) (maxVal / measureUnit) + 1;
		float horizontalWindowRange = (p.width - border.sides);
		float horizontalWindowGap = horizontalWindowRange / horizontalIntervals;
		float verticalWindowRange = p.height - border.topBottom;
		float verticalWindowGap = verticalWindowRange / verticalIntervals;
		float tickSize = border.avgBorder * 0.1f;	//Size of indicators

		//Styling
		p.pushStyle();
		p.stroke(200, 200, 200);
		p.fill(200, 200, 200);
		p.strokeWeight(1);
		p.textSize(12);

		//Horizontal Line
		p.line(border.left, p.height - border.bottom, p.width - border.right, p.height - border.bottom);

		//Draw the indicators & descripctors
		//Keys may be null if no data has been selected, hence we must check it.
		if(keys != null){
			Iterator<String> it = keys.iterator();	//Create iterator to iterate over keys
			for(int i = 0; i <=  horizontalIntervals; i++){
				float textX = border.left + (i * horizontalWindowGap);
				float textY = p.height - (border.bottom * 0.75f);

				//Draw indicator
				p.line(textX, p.height - (border.bottom - tickSize), textX, (p.height - border.bottom));

				//Print the description
				p.textAlign(PApplet.CENTER, PApplet.BOTTOM);
				p.text(it.next(), textX, textY);
			}
		}

		// Draw the vertical axis
		p.line(border.left, border.top , border.left, p.height - border.bottom);

		//Draw vertical indicators
		for(int i = 0; i <= verticalIntervals; i++){
			float y = (p.height - border.bottom) - (i * verticalWindowGap);

			//Draw indicator
			p.line(border.left - tickSize, y, border.left, y);

			//Calculate & display the unit
			String unit = Integer.toString(i * measureUnit);
			p.textAlign(PApplet.RIGHT, PApplet.CENTER);
			p.text(unit, border.left - (tickSize * 2.0f), y);
		}

		//Replace previous styling
		p.popStyle();
	}

	//Calc the measureUnit. This is the value the lables on the vertical axis will increment by
	int calcMeasureUnit(){
		int measureUnit;

		if(maxVal < 10) measureUnit = 1;
		else if(maxVal < 50) measureUnit = 10;
		else if(maxVal < 500) measureUnit = 50;
		else measureUnit = 100;

		return measureUnit;
	}

	//Returns the maximum value of the data supplied rounded up to the nearest measureUnit
	int getMaxVal(){
		return (((maxVal + measureUnit - 1) / measureUnit) * measureUnit);
	}
}
