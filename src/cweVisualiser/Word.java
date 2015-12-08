package cweVisualiser;
/*
 * This class stores everything we need for a word in the word cloud
 * 
 * Author: Conor Devilly
 * Date: 20151128
 */
import processing.core.*;

public class Word {
	
	//Fields
	String str;
	PVector pos;
	float size;
	float width;
	float halfW;
	float ascent;
	float descent;
	int colour;

	//Constructor
	Word(String str, PVector pos, float size, float width, float ascent, float descent, int colour){
		this.str = str;
		this.pos = pos;
		this.size = size;
		this.width = width;
		halfW = width / 2;
		this.ascent = ascent;
		this.descent = descent;
		this.colour = colour;
	}
}
