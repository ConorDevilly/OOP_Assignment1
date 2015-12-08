package cweVisualiser;

/*
 * Loads the data from the XML files into an SQLite database.
 * This program exists externally from the main program as it only has to run once.
 * If you want to run this file, you'll have to download all the (XML) NVD CVE data from 2003 - 2015 located at:
 * https://nvd.nist.gov/download.cfm 
 * 
 * These files will need to be put in a seperate folder named "Data"
 * 
 * Author: Conor Devilly
 * Date: 20151109
 */

import processing.core.*;
import processing.data.XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class SQLLoader extends PApplet{

	//Fields
	SQL db = new SQL();
	XML xmlDoc;

	public void setup(){
		int startYear = 2003;
		int endYear = 2015;

		db.dropTables();
		db.createTables();

		ArrayList<CWE> cweDefs = readCWEDefs();
		readCWERecs(startYear, cweDefs, endYear);

		db.closeConnection();
		println("Data import successful");
	}

	//Read in data from CWE definition file
	ArrayList<CWE> readCWEDefs(){
		xmlDoc = loadXML("../Data/CWEs.xml");
		XMLReader reader = new XMLReader(this, xmlDoc);
		ArrayList<CWE> defs = reader.getCWEDefs();

		for(CWE cwe : defs){
			//TODO: Escape input
			db.insertRec(cwe.id, cwe.name, cwe.desc, cwe.cmnName, cwe.parent);
		}
		return defs;
	}

	//Read in entries from xml record files
	void readCWERecs(int startYear, ArrayList<CWE> cweDefs, int endYear){

		for(int i = startYear; i <= endYear; i++){
			xmlDoc = loadXML("../Data/nvdcve-2.0-" + Integer.toString(i) + ".xml");
			XMLReader reader = new XMLReader(this, xmlDoc);
			HashMap<String, Integer> cweList = reader.getCWE(cweDefs);

			for(Entry<String, Integer> e : cweList.entrySet()){
				//TODO: Escape input
				db.insertRec(e.getKey(), i, e.getValue());
			}
		}
	}
}
