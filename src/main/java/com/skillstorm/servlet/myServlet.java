package com.skillstorm.servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.beans.Apartment;
import com.skillstorm.data.ApartmentDAO;

@WebServlet(urlPatterns = "/apartments")
public class myServlet extends HttpServlet {
	
	private ApartmentDAO dao = new ApartmentDAO();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Apartment> apmList = dao.findAll();
		System.out.println("Apartments Listed");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(apmList);
		resp.getWriter().print(json);
		resp.setContentType("application/json");
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Apartment apm = mapper.readValue(req.getInputStream(), Apartment.class);
		apm = dao.save(apm);
		if(apm != null) {
			String json = mapper.writeValueAsString(apm);
			resp.getWriter().print(json);
			resp.setContentType("application/json");
			resp.setStatus(201);
			System.out.println("Inserted Apartment!");
		}
		else {
			resp.setStatus(500);
			System.out.println("Inserted Apartment failed...");
		}
		
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Apartment apm = mapper.readValue(req.getInputStream(), Apartment.class);
		if(dao.update(apm)) {
			resp.setStatus(201);
			System.out.println("Updated Apartment!");
			resp.setContentType("application/json");
		}
		else {
			resp.setStatus(500);
			System.out.println("Update Apartment failed...");
		}
		
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			int id = Integer.parseInt(req.getParameter("id"));
			if(dao.delete(id)) {
				resp.setStatus(201);
				System.out.println("Deleted Apartment!");
				resp.setContentType("application/json");
			}
			else {
				System.out.println("Deletion failed...");
			}
		} catch (Exception e) {
			resp.setStatus(500);
			System.out.println("ID was not recongnize");
		}
	}
}
