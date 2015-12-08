package cweVisualiser;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import processing.core.*;

import processing.core.PApplet;

/*
 * Creates a word cloud
 * 
 * Author: Conor Devilly
 * Date: 20151112
 */

public class WordCloud extends Graph{

	//Fields
	ArrayList<Word> words;

	//Constructor
	WordCloud(PApplet p, LinkedHashMap<String, Integer> data) {
		super(p, data);
		words = new ArrayList<Word>();
	}

	//Checks if the word is colliding with another word
	boolean chkCollision(Word p1, PVector p2, float ascent, float descent, float widthP2) {
		if (

				//Checks if the x coords of the two words intersect
				(
				 (p2.x + widthP2) > (p1.pos.x - p1.halfW) 
				 && (p2.x - widthP2) < (p1.pos.x + p1.halfW)
				)

				&& 

				//Checks if the y coords of the two words intersect
				(
				 (p2.y + ascent) > (p1.pos.y - p1.descent) 
				 && (p2.y - descent) < (p1.pos.y + p1.ascent)
				) 

		   ) {
			return true;
		} else {
			return false;
		}
	}

	//Calculates the coordinates of each word in the cloud
	//This takes a while to run
	void calcWordCloud(){
		float cx = p.width / 2;
		float cy = p.height / 2;


		for(Entry<String, Integer> e : data.entrySet()){
			//Reset theta every time as a smaller word may fit where a previous one wouldn't
			float theta = 0f;
			float rotRad = 0f;
			String str = e.getKey();
			
			//Set the text size proportionally to the the CWE's count
			float textSize = PApplet.map(e.getValue(), 0, getMaxVal(), 10, 70);

			p.textSize(textSize);
			
			//The position we're trying to place it in
			PVector	cmp = new PVector(cx + PApplet.sin(theta) * rotRad, cy - PApplet.cos(theta) * rotRad );

			//Check if it collides with any other word
			for(int i = 0; i <= words.size() - 1; i++){
				Word pos = words.get(i);
				
				if(chkCollision(pos, cmp, p.textAscent(), p.textDescent(), p.textWidth(str))){
					//Change the position (in a spiraling direction) if there is a collision
					theta += 1f;
					rotRad += 0.5f;
					cmp = new PVector(cx + PApplet.sin(theta) * rotRad, cy - PApplet.cos(theta) * rotRad );
					
					//We need to check every result again if we moved the word
					i = -1; 
				}
			}
			//Generate a colour and add the coordinates & associated data to the list
			int col = p.color(p.random(0, 255), 127, p.random(0, 255));
			Word addition = new Word(str, cmp, textSize, p.textWidth(str), p.textAscent(), p.textDescent(), col);
			words.add(addition);
		}
	}

	//Displays the word cloud
	void drawWordCloud(){
		for(Word w : words){
			p.textSize(w.size);
			p.textAlign(PApplet.CENTER, PApplet.TOP);
			p.text(w.str, w.pos.x, w.pos.y);
			p.fill(w.colour);

		}
	}
}
