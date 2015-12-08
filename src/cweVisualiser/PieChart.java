package cweVisualiser;
/*
 * Draw a pie chart for a given year
 * 
 * Author: Conor Devilly
 * Date: 20151207
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import processing.core.*;

public class PieChart extends Graph {

	//Fields
	ArrayList<Integer> colours;

	//Constructor
	PieChart(PApplet p, LinkedHashMap<String, Integer> data) {
		super(p, data);
		colours = new ArrayList<Integer>();
		for(int i = 0; i < data.size(); i++){
			colours.add(p.color(p.random(0, 255), p.random(0, 255), p.random(0, 255)));
		}
	}

	//Display the pie chart
	void drawPieChart(){
		
		// Calculate the sum
		float sum = 0.0f;
		for(Entry<String, Integer> e : data.entrySet())
		{
			sum += e.getValue();
		}
		
		//Display info about the year
		p.textSize(18);
		p.fill(p.color(127, 127, 127));
		p.text("Total Vulnerabilities: " + String.format("%.0f", sum), p.width / 2, 75);
		p.text("Average Vulnerabilities per category: " + (int)(sum / data.size()), p.width / 2, 75 + p.textAscent() + p.textDescent());


		// Calculate the angle to the mouse
		float toMouseX = p.mouseX - p.width / 2;
		float toMouseY = p.mouseY - p.height / 2;  
		float angle = PApplet.atan2(toMouseY, toMouseX);  
		//If the angle is less than zero we need to map it
		if (angle < 0)
		{
			angle = PApplet.map(angle, -PApplet.PI, 0, PApplet.PI, PApplet.TWO_PI);
		}

		// The last angle
		float last = 0;

		// The cumulative sum of the dataset 
		float cumulative = 0;
		Iterator<Entry<String, Integer>> it = data.entrySet().iterator();
		for(int i = 0 ; i < data.size(); i ++)
		{
			Entry<String, Integer> e = it.next();
			cumulative += e.getValue(); 
			// Calculate the current angle
			float current = PApplet.map(cumulative, 0, sum, 0, PApplet.TWO_PI);
			// Draw the pie segment
			p.pushStyle();
			p.stroke(colours.get(i));
			p.fill(colours.get(i));

			float r = 300;

			// If the mouse angle is inside the pie segment
			if (angle > last && angle < current)
			{
				float percent = (e.getValue() / sum) * 100;
				r = r * 1.25f;
				p.textSize(18);
				p.text(e.getKey(), p.mouseX + 25, p.mouseY + 25);
				p.text(e.getValue(), p.mouseX + 25, p.mouseY + 25 + p.textAscent() + p.textDescent());
				p.text(String.format("%.2f%%", percent), p.mouseX + 25, p.mouseY + 25 + 2* (p.textAscent() + p.textDescent()));
			}

			// Draw the arc
			p.arc(p.width / 2, p.height / 2 + 50, r, r, last, current);
			last = current;       
		}

		p.stroke(255);
		//p.line(p.width / 2, p.height / 2, p.mouseX, p.mouseY);
		p.popStyle();
	}
}
