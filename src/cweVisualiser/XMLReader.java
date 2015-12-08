package cweVisualiser;
/*
 * Reads CWE data from XML document
 * 
 * Author: Conor Devilly
 * Date: 20151109
 */

import java.util.ArrayList;
import java.util.HashMap;
import processing.data.XML;
import processing.core.PApplet;

public class XMLReader{

	//Fields
	XML xmlDoc;
	PApplet parent;

	//Constructor
	XMLReader(PApplet p, XML xmlDoc){
		this.parent = p;
		this.xmlDoc = xmlDoc;
	}

	//Returns a list of results from the CWEs.xml file
	ArrayList<CWE> getCWEDefs(){
		ArrayList<CWE> defs = new ArrayList<CWE>();
		XML[] cwedefs = xmlDoc.getChildren("CWE");

		for(XML e : cwedefs){
			String id = "CWE-" + e.getString("id");
			String name = e.getChild("Name").getContent();
			String desc = e.getChild("Desc").getContent();
			String cmnName = e.getChild("cmnName").getContent();
			String parent = e.getChild("Parent").getString("id");
			CWE cwe = new CWE(id, name, desc, cmnName, parent);
			defs.add(cwe);
		}

		return defs;
	}

	//Returns an id and count for every CWE in every year
	HashMap<String, Integer> getCWE(ArrayList<CWE> cweDefs){
		HashMap<String, Integer> cweCount = new HashMap<String, Integer>();
		XML[] cweList = xmlDoc.getChildren("entry/vuln:cwe");

		for(int i = 0; i < cweList.length; i++){
			String id = cweList[i].getString("id");

			//If we come across an id that we already have, we increment its count
			if(cweCount.containsKey(id)){
				int curVal = cweCount.get(id);
				curVal++;
				cweCount.put(id, curVal);
			}else{
				cweCount.put(id, 1);
			}
		}

		//Initialize any not found values to 0
		for(CWE e : cweDefs){
			if(!cweCount.containsKey(e.id)){
				cweCount.put(e.id, 0);
			}
		}

		return cweCount;
	}
}
