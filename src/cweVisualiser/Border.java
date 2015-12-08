package cweVisualiser;
/*
 * Contains 4 values that will be used as the border on a graph
 * 
 * Author: Conor Devilly
 * Date: 20151109
 */

public class Border {

	//Fields
	float left;
	float right;
	float top;
	float bottom;
	float sides;
	float topBottom;
	float avgBorder;

	//Constructor
	Border(float l, float r, float t, float b){
		left = l;
		right = r;
		top = t;
		bottom = b;
		sides = left + right;
		topBottom = top + bottom;
		avgBorder = ((left + right + top + bottom) / 4);
	}
}
