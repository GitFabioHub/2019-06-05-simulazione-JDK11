package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Crimine;
import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public Collection<Integer>getAnni(){
		String sql = "SELECT DISTINCT  YEAR(e.reported_date) as year " + 
				"FROM EVENTS e " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(
							res.getInt("year"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public Collection<Adiacenza> getAdiacenze(int anno) {

		String sql = "SELECT DISTINCT e.district_id as id, AVG(e.geo_lat) AS lat, AVG(e.geo_lon) AS lon " + 
				"FROM EVENTS e " + 
				"WHERE YEAR(e.reported_date)= ? " + 
				"GROUP BY e.district_id " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			List<Adiacenza> list = new ArrayList<>() ;
			Map<Integer,LatLng>map=new TreeMap<>();
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					LatLng centro=new LatLng(res.getDouble("lat"),res.getDouble("lon"));
					int id=res.getInt("id");
					map.put(id, centro);
		
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			conn.close();
			
			for(Integer i:map.keySet()) {
				for(Integer l:map.keySet()) {
					
					if(i!=l) {
					double distanza= LatLngTool.distance(map.get(i),map.get(l),LengthUnit.KILOMETER);
					list.add(new Adiacenza(i,l,distanza));
					}
				}
			}	
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	public Collection<Crimine> getCrimini(int mese,int anno,int giorno){
		
		
		String sql = "SELECT   e.offense_category_id AS offense,e.district_id AS dist,cast(e.reported_date AS TIME) AS time " + 
				"FROM EVENTS e " + 
				"WHERE YEAR(e.reported_date)= ? AND MONTH(e.reported_date)= ? AND DAY(e.reported_date)= ? " + 
				"ORDER BY e.reported_date asc" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2,mese);
			st.setInt(3,giorno);
			
			List<Crimine> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					Crimine c=new Crimine(res.getString("offense"),res.getInt("dist"),res.getTime("time").toLocalTime());
					list.add(c);
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
    }
	
	
	public int getLowRateDist(int anno) {
		String sql = "SELECT  e.district_id AS id,COUNT(incident_id) as peso " + 
				"FROM EVENTS e " + 
				"WHERE YEAR(e.reported_date)= ? " + 
				"GROUP BY e.district_id ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1,anno);
			
			Map<Integer,Integer> list = new TreeMap<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.put(res.getInt("id"),res.getInt("peso"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			conn.close();
			int min=1000000000;
			int dist=0;
			for(int i:list.keySet()) {
				if (list.get(i)<min)
					dist=i;
			}
			
			return dist ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0 ;
		}
		
	}
	
	
	
	
	
	
	
}
