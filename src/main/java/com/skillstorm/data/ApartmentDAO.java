package com.skillstorm.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.skillstorm.beans.Apartment;

public class ApartmentDAO{
	
	private String url = "jdbc:mysql://localhost:3306/apartments";
	private String user = "root";
	private String password = "root";
	
	private String tableName = "apartment";
	
	static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver error");
            e.printStackTrace();
        }
    }
	
	public ApartmentDAO(){

	}
	
	public ApartmentDAO(String tableName) {
		this.tableName = tableName;
	}
	

	public Apartment save(Apartment apartment) {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "INSERT INTO "+tableName+"(NAME, ADDRESS, PRICE) VALUES(?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, apartment.getName());
			statement.setString(2, apartment.getAddress());
			statement.setInt(3, apartment.getPrice());
			int rows = statement.executeUpdate();
			ResultSet keys = statement.getGeneratedKeys();
			keys.next();
			int id = keys.getInt(1);
			apartment.setId(id);
			return apartment;
		} catch (Exception e) {
			System.out.println("Insertion failed");
            e.printStackTrace();
		}
		return null;
	}

	public List<Apartment> findAll() {
		List<Apartment> results = new LinkedList<>();
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "SELECT APARTMENT_ID, NAME, ADDRESS, PRICE FROM " + tableName;

			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Apartment apm = new Apartment(rs.getString("NAME"), rs.getString("ADDRESS"), rs.getInt("PRICE"));
				apm.setId(rs.getInt("APARTMENT_ID"));
				results.add(apm);
			}
		} catch (Exception e) {
			System.out.println("Find all failed");
            e.printStackTrace();
		}
		return results;
	}
	
	public boolean update(Apartment apartment) {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "update " +tableName+ " set NAME = ?, ADDRESS = ?, PRICE = ? where APARTMENT_ID = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, apartment.getName());
			statement.setString(2, apartment.getAddress());
			statement.setInt(3, apartment.getPrice());
			statement.setInt(4, apartment.getId());
			int rows = statement.executeUpdate();
			return rows > 0 ? true: false;
		} catch (Exception e) {
			System.out.println("Update failed");
            e.printStackTrace();
		}
		return false;
	}
	
	public boolean delete(int id) {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "delete from "+tableName+" where APARTMENT_ID = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			int rows = statement.executeUpdate();
			return rows > 0 ? true: false;
		} catch (Exception e) {
			System.out.println("Deletion failed");
            e.printStackTrace();
		}
		return false;
	}
	
}

