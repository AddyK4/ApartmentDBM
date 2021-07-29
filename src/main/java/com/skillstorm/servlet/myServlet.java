package com.skillstorm.servlet;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.beans.Apartment;

@WebServlet(urlPatterns = "/apartments")
public class myServlet extends HttpServlet {
	
	private CopyOnWriteArrayList<Apartment> apmList = new CopyOnWriteArrayList<>();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GET");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(apmList);
		resp.getWriter().print(json);
		resp.setContentType("application/json");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Apartment apm = mapper.readValue(req.getInputStream(), Apartment.class);
		apmList.add(apm);
		resp.setStatus(201);
		System.out.println("Inserted Apartment!");
	}
}
