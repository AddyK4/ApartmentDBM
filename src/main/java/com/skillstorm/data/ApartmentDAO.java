package com.skillstorm.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.skillstorm.beans.Apartment;

public class ApartmentDAO implements ApartmentDAOInterface, AutoCloseable{
	
	private Connection connection;
	
	public ApartmentDAO() throws Exception {
		connect();
	}

	@Override
	public void close() throws Exception {
		if(connection != null && !connection.isClosed()) {
			this.connection.close();
		}
	}
	
	public void connect() throws Exception {
		String url = "jdbc:mysql://localhost:3306/apartments";
		String user = "root";
		String password = "root";
		
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		this.connection = DriverManager.getConnection(url, user, password);
	}

	@Override
	public boolean save(Apartment apartment) throws SQLException {
		String sql = "INSERT INTO HOME(NAME, ADDRESS, VALUE) VALUES(?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, apartment.getName());
		statement.setString(2, apartment.getAddress());
		statement.setInt(3, apartment.getPrice());
		int rows = statement.executeUpdate();
		return rows > 0 ? true: false;
	}

	@Override
	public List<Apartment> findAll() throws SQLException {
		String sql = "SELECT APARTMENT_ID, NAME, ADDRESS, PRICE FROM HOME";
		List<Apartment> results = new LinkedList<>();
		PreparedStatement stmt = connection.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			Apartment apm = new Apartment(rs.getString("NAME"), rs.getString("ADDRESS"), rs.getInt("PRICE"));
			apm.setId(rs.getInt("HOME_ID"));
			results.add(apm);
		}
		return results;
	}
	
}

interface ApartmentDAOInterface {
	// create
	public boolean save(Apartment apartment) throws SQLException;
	
	// get
	public List<Apartment> findAll() throws SQLException;
}