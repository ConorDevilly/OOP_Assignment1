package cweVisualiser;

/*
 * Encapsulates data from "CWEs.xml"
 * This file contains a details about each CWE.
 * 
 * Author: Conor Devilly
 * Date: 20151109
 */

public class CWE {
	//Fields
	String id;
	String name;
	String desc;
	String cmnName;
	String parent;

	//Constructor
	CWE(String id, String name, String desc, String cmnName, String parent){
		this.id = id;
		this.name = name; 
		this.desc = desc;
		this.cmnName = cmnName;
		this.parent = parent;
	}
}
