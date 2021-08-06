package com.skillstorm.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.skillstorm.beans.Apartment;
import com.skillstorm.data.ApartmentDAO;

public class DatatableTest {
	
	ApartmentDAO dao = new ApartmentDAO("apartmenttest");

	private String url = "jdbc:mysql://localhost:3306/apartments";
	private String user = "root";
	private String password = "root";
	
	final Apartment apmTest = new Apartment("Apm Test Name", "Apm Test Address", 1234);
	
	@Test
	public void createTest() {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			System.out.println("Create test method called"); //DAO create method here
			Apartment apm = new Apartment("Apm Test Name 2", "Apm Test Address 2", 4321);
			dao.save(apm);
		} catch (Exception e) {
			System.out.println("Create Test failed");
			e.printStackTrace();
			fail();
		}
		assertTrue(true);
	}
	
	@Test
	public void retrievalTest() {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			System.out.println("Retrieve test method called");
			List<Apartment> apmList = dao.findAll(); //DAO retrieve method here
			assertTrue(apmList.get(0).getName().equals(apmTest.getName()));
		} catch (Exception e) {
			System.out.println("Retrieve Test failed");
			e.printStackTrace();
			fail();
		}
		
	}
	
	@Test
	public void updateTest() {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			System.out.println("Update test method called");
			
			//Creating Apartment with different values
			Apartment apmUT = new Apartment("apmUN", "apmUA", 1111);
			apmUT.setId(1);
			dao.update(apmUT); //DAO update call here
			
			//Process of grabbing the updated entry by running the create DAO method 
			List<Apartment> updatedEntry = new LinkedList<>();
			String sql = "SELECT APARTMENT_ID, NAME, ADDRESS, PRICE FROM apartmenttest";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			Apartment apm = new Apartment(rs.getString("NAME"), rs.getString("ADDRESS"), rs.getInt("PRICE"));
			apm.setId(rs.getInt("APARTMENT_ID"));
			updatedEntry.add(apm);
			
			//Checks if the entry that I grabbed from the DB is not equal to the expected Apartment entry
			assertTrue(updatedEntry.get(0) != apmTest);
		} catch (Exception e) {
			System.out.println("Update Test failed");
			e.printStackTrace();
			fail();
		}

	}
	
	@Test
	public void deleteTest() {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			System.out.println("Delete test method called");
			dao.delete(1); //DAO delete method here
			List<Apartment> deletedAPM = dao.findAll();
			assertTrue(deletedAPM.size() == 0);
		} catch (Exception e) {
			System.out.println("Delete Test failed");
			e.printStackTrace();
			fail();
		}
	}
	
	@Before
	public void beforeTest() {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			System.out.println("Before Test called");
			
			//DDL
			String sql = "create table apartmentTest (\r\n"
					+ "	APARTMENT_ID INT PRIMARY KEY auto_increment,\r\n"
					+ "	NAME VARCHAR(50),\r\n"
					+ "	ADDRESS VARCHAR(50),\r\n"
					+ "	PRICE INT\r\n"
					+ ")";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.executeUpdate();
			
			//DML
			sql = "INSERT INTO apartmenttest(NAME, ADDRESS, PRICE) VALUES(?, ?, ?)";
			statement = conn.prepareStatement(sql);//, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, apmTest.getName());
			statement.setString(2, apmTest.getAddress());
			statement.setInt(3, apmTest.getPrice());
			statement.executeUpdate();
			
			System.out.println("Apartment Test Table and Mock Entries Inserted");
		} catch (Exception e) {
			System.out.println("Insertion failed");
            e.printStackTrace();
            fail();
		}
	}
	
	@After
	public void afterTest() {
		try(Connection conn = DriverManager.getConnection(url, user, password)) {
			//wait(1000);
			String sql = "Drop table apartmenttest";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			System.out.println("Table dropped for After Test\n");
			conn.close();
		} catch (Exception e) {
			System.out.println("After Test failed");
			e.printStackTrace();
			fail();
		}
	}
}
