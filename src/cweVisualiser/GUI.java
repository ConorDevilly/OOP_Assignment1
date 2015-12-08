package cweVisualiser;
/*
 * This class is the GUI for the program
 * It is implemented using ControlP5 / Processing
 * This is the main class that drives the program
 * 
 * Author: Conor Devilly
 * Date: 20151128
 */

import processing.core.*;

import java.util.ArrayList;

import controlP5.*;

public class GUI extends PApplet{
	ControlP5 ctrl;
	SQL db;
	String mode; //Controls what mode the graph is in
	GraphController gCtrl;
	Border border;
	ArrayList<Boolean> active;
	Icon lineGraphIcon;
	Icon wordCloudIcon;
	Icon pieChartIcon;
	Slider yearSlider;
	ScrollableList CWENameList;

	//Setup
	public void setup(){
		//The screen has to be big as we have a lot of data to look at
		size(1300, 670);

		ctrl = new ControlP5(this);
		db = new SQL();
		border = new Border(200, 200, 30, 200);
		gCtrl = new GraphController(this, db, border);
		active = new ArrayList<Boolean>();

		background(0);
		mode = "Cloud";
		setupGUI();

	}

	//Draw different screens based on what mode the program is in
	public void draw(){
		int year = (int) yearSlider.getValue();
		switch(mode){
			case "Line":
				background(0);
				gCtrl.drawLineGraphs();
				break;
			case "Cloud":
				gCtrl.drawWordCloud(year);
				textSize(24);
				fill(color(127, 127, 127));
				text(year, width / 2, 30);
				break;
			case "Pie":
				gCtrl.drawPieChart(year);
				textSize(24);
				fill(color(127, 127, 127));
				text(year, width / 2, 50);
				break;
		}
	}

	//Listens for user events (e.g. user clicking on button) and does something based on the event
	public void controlEvent(ControlEvent e){
		switch(e.getId()){
			//Line graph icon clicked
			case(1):
				mode = "Line";
				yearSlider.hide();
				CWENameList.show();
				background(0);
				break;
				//Word cloud icon clicked
			case(2):
				mode = "Cloud";
				CWENameList.hide();
				yearSlider.show();
				background(0);
				break;
				//CWE Name clicked in Line Graph mode
			case(3):
				int n = (int)e.getController().getValue();
				String id = (String) CWENameList.getItem(n).get("name");
				String name = db.getCWEIdFromByCmnName(id);
				//Toggle whether the graph appears on the screen
				if(!active.get(n)){
					//Colour red
					CColor c = new CColor();
					c.setBackground(color(255, 0, 0));
					c.setForeground(color(0, 116, 217));
					c.setActive(color(255, 0, 0));
					CWENameList.getItem(n).put("color", c);
					gCtrl.addLineGraph(name);
				}else{
					//Colour normal
					CColor c = new CColor();
					c.setBackground(color(0, 45, 90));
					c.setForeground(color(0, 116, 217));
					c.setActive(color(0, 45, 90));
					CWENameList.getItem(n).put("color", c);
					gCtrl.removeLineGraph(name);
				}
				active.set(n, !active.get(n));
				break;
			case(5):
				mode = "Pie";
				CWENameList.hide();
				yearSlider.show();
				break;
		}
	}

	//Draw elements of the GUI
	void setupGUI(){
		//The line graph icon
		lineGraphIcon = ctrl.addIcon("lineGraph",10)
			.setPosition(30, 100)
			.setSize(50, 50)
			.setFont(createFont("fontawesome-webfont.ttf", 40))
			.setFontIcon(0xf201)
			.setFontIconSize(30)
			.setId(1)
			;

		//Word Cloud icon
		wordCloudIcon = ctrl.addIcon("worldCloud",10)
			.setPosition(30, 50)
			.setSize(50, 50)
			.setRoundedCorners(0)
			.setFont(createFont("fontawesome-webfont.ttf", 40))
			.setFontIcon(0xf0c2)
			.setFontIconSize(30)
			.setId(2)
			;

		pieChartIcon = ctrl.addIcon("pieChart", 10)
			.setPosition(30, 150)
			.setSize(50, 50)
			.setRoundedCorners(0)
			.setFont(createFont("fontawesome-webfont.ttf", 40))
			.setFontIcon(0xf200)
			.setFontIconSize(30)
			.setId(5)
			;

		//List of CWE Names to control the graphs shown in Line mode
		CWENameList = ctrl.addScrollableList("CWENameList")
			.setPosition(width - 160, 50)
			.setSize(100, 200)
			.setBarHeight(20)
			.setWidth(150)
			.setItemHeight(20)
			.setType(ScrollableList.LIST)
			.setCaptionLabel("CWE Name")
			.setId(3)
			.hide()
			;

		//Get list of CWE Names & add them to the list
		ArrayList<String> cweIds = db.getCWEDefData("cmnName");
		ctrl.get(ScrollableList.class, "CWENameList").addItems(cweIds);

		//For each entry we create a boolean that tracks if the graph is active or not
		for(String s : cweIds){
			active.add(false);
		}

		//Year Slider to control the year in Cloud mode
		yearSlider = ctrl.addSlider("yearSlider")
			.setPosition(width - 100, 50)
			.setWidth(30)
			.setHeight(400)
			.setRange(2003, 2015)
			.setNumberOfTickMarks((2015 - 2003) + 1)
			.setValue(2015)
			.setSliderMode(Slider.FLEXIBLE)
			.setDecimalPrecision(0)
			.setCaptionLabel("Year")
			.setId(4)
			.show()
			;
	}
}
