package cweVisualiser;

/*
 * This class contains all SQL related code
 * 
 * Author: Conor Devilly
 * Date: 20151109
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SQL{ 
	Connection c;

	//Creates a connection to the db once the instance is created
	public SQL(){
		c = connectToDb();
		if(c == null) 
			System.err.println("ERROR: Cannot connect to DB");
	}

	//Connects to the Database
	Connection connectToDb(){
		Connection c = null;
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:cwe.db");
		}catch(Exception e){
			e.printStackTrace();
		}
		return c;
	}

	//Deletes all tables from the database
	void dropTables(){
		Statement stmt = null;
		try {
			stmt = c.createStatement();

			String dropDesc = "DROP TABLE CWEDesc;";
			String dropRecs = "DROP TABLE CWERecs;";

			stmt.execute(dropDesc);
			stmt.execute(dropRecs);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
	}

	//Creates tables
	void createTables(){
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String createDesc =
				"CREATE TABLE CWEDesc("
				+ "id VARCHAR(7) NOT NULL,"
				+ "name VARCHAR(50) NOT NULL,"
				+ "desc VARCHAR(250) NOT NULL,"
				+ "cmnName VARCHAR(30) NOT NULL,"
				+ "parent VARCHAR(7),"
				+ "PRIMARY KEY (id)"
				+ ");";

			String createRecs = 
				"CREATE TABLE CWERecs("
				+ "id VARCHAR(7) NOT NULL,"
				+ "year INTEGER(4) NOT NULL,"
				+ "count INTEGER(5) NOT NULL,"
				+ "PRIMARY KEY (id, year),"
				+ "FOREIGN KEY (id) REFERENCES CWEDesc(id)"
				+ ");";
			//TODO: Add way to check year is a year
			stmt.execute(createDesc);
			stmt.execute(createRecs);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
	}

	//Insert CWEDesc
	void insertRec(String id, String name, String desc, String cmnName, String parent){
		Statement stmt = null;
		try{
			stmt = c.createStatement();
			String sql = 
				"INSERT INTO CWEDesc(id, name, desc, cmnName, parent)"
				+ "VALUES("
				+ "'" + id + "',"
				+ "'" + name + "',"
				+ "'" + desc + "',"
				+ "'" + cmnName + "',"
				+ "'" + parent + "'"
				+ ");";
			stmt.execute(sql);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
	}
	//Insert CWERec
	void insertRec(String id, int year, int count){
		Statement stmt = null;
		try{
			stmt = c.createStatement();
			String sql = "INSERT INTO CWERecs(id, year, count)"
				+ "VALUES("
				+ "'" + id + "',"
				+ year + ","
				+ count
				+ ");";
			stmt.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
	}

	//Return a list of CWE-IDs, names or cmnNames, depending on what is supplied to method
	ArrayList<String> getCWEDefData(String query){
		//Check that the query parameter is valid
		if(!query.matches("id|name|cmnName")){
			return null;
		}else{
			Statement stmt = null;
			ArrayList<String> retList = new ArrayList<String>();
			try{
				stmt = c.createStatement();
				ResultSet res = stmt.executeQuery(
						"SELECT " + query + " FROM CWEDesc;");

				while(res.next()){
					retList.add(res.getString(1));
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
			}
			return retList;
		}
	}

	//Returns all the years stored in the db
	ArrayList<String> getYears(){
		Statement stmt = null;
		ArrayList<String> retList = new ArrayList<String>();
		try{
			stmt = c.createStatement();
			ResultSet res = stmt.executeQuery("SELECT year FROM CWERecs GROUP BY year ORDER BY year DESC;");

			while(res.next()){
				retList.add(res.getString(1));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
		return retList;
	}

	//Translates a common name to an id
	String getCWEIdFromByCmnName(String cmnName){
		Statement stmt = null;
		String id = null;
		try{
			stmt = c.createStatement();
			ResultSet res = stmt.executeQuery(
					"SELECT id FROM CWEDesc WHERE cmnName = " + "'" + cmnName + "';");

			while(res.next()){
				id = res.getString(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
		return id;
	}

	//Returns a list of IDs and their counts for a given year
	LinkedHashMap<String, Integer> getCWERecsByYear(int year){
		Statement stmt = null;
		LinkedHashMap<String, Integer> retList = new LinkedHashMap<String, Integer>();

		try{
			stmt = c.createStatement();
			String sql =
				"SELECT d.cmnName, r.count FROM CWERecs r JOIN CWEDesc d ON (d.id = r.id) WHERE year = "+ Integer.toString(year) + " ORDER BY count DESC;";
			ResultSet res = stmt.executeQuery(sql);

			while(res.next()){
				String descriptor = res.getString(1);
				int total = res.getInt(2);
				retList.put(descriptor, total);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
		return retList;
	}

	//Returns a description for a given id
	ArrayList<String> getCWEDescById(String id){
		Statement stmt = null;
		ArrayList<String> retList = new ArrayList<String>();

		try{
			stmt = c.createStatement();
			String sql =
				"SELECT id, name, desc FROM CWEDesc WHERE id = '" + id + "'" + ";";
			ResultSet res = stmt.executeQuery(sql);

			while(res.next()){
				retList.add(res.getString(1));
				retList.add(res.getString(2));
				retList.add(res.getString(3));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
		return retList;
	}

	//Returns a list of Years and the CWE count for each year for a given id
	public LinkedHashMap<String, Integer> getCWERecsByID(String id){
		Statement stmt = null;
		LinkedHashMap<String, Integer> retList = new LinkedHashMap<String, Integer>();

		try{
			stmt = c.createStatement();
			String sql =
				"SELECT year, count FROM CWERecs WHERE id = '" + id + "'" + ";";
			ResultSet res = stmt.executeQuery(sql);

			while(res.next()){
				String descriptor = res.getString(1);
				int total = res.getInt(2);
				retList.put(descriptor, total);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try { if (stmt != null) stmt.close(); } catch (Exception e) {e.printStackTrace();};
		}
		return retList;
	}

	//Close the connection to the DB
	public void closeConnection(){
		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
