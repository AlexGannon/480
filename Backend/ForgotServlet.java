package com.glasses.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.codec.binary.Base64;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;



@SuppressWarnings("serial")
public class ForgotServlet extends HttpServlet {
	Random random = new Random();
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("");
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user = req.getParameter("user").toLowerCase();
		String email = req.getParameter("email").toLowerCase();
		String type = req.getParameter("type");
		
		
		JSONObject jsonresp = new JSONObject();
		PrintWriter printout = resp.getWriter();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		if(type.equals("getQuestion"))
		{
	    Filter userFilter = new FilterPredicate("username", FilterOperator.EQUAL, user);
	    Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
	    Filter compFilter = CompositeFilterOperator.and(userFilter, emailFilter);
	    
	    Query q = new Query("user").setFilter(compFilter);

	    PreparedQuery pq = datastore.prepare(q);

	    Entity userEntity = null;
	    
	    for (Entity result : pq.asIterable()) {
	    	  userEntity = result;
	    	}
	    
	    if(!(userEntity == null))
	    {	
	    	if(userEntity.getProperty("locked").toString().equals("true"))
	    	{	
	    		try {
    			jsonresp.put("status", "locked");
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    		
	    	}
	    	else
	    	{
	    
	    		try {
	    			jsonresp.put("status", "valid");
					jsonresp.put("question", userEntity.getProperty("question"));
					jsonresp.put("answer", userEntity.getProperty("answer"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    	}
	    	
	    }
	    else
	    {
    		try {
				jsonresp.put("status", "invalid");
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    }
		}
		else
		{
			Filter userFilter = new FilterPredicate("username", FilterOperator.EQUAL, user);
		    Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email.toLowerCase());
		    Filter compFilter = CompositeFilterOperator.and(userFilter, emailFilter);
		    
		    Query q = new Query("user").setFilter(userFilter);

		    PreparedQuery pq = datastore.prepare(q);

		    Entity userEntity = null;
		    
		    for (Entity result : pq.asIterable()) {
		    	  userEntity = result;
		    	}
		    
		    if(userEntity != null)
		    {
		    	userEntity.setProperty("locked", "true");
		    	datastore.put(userEntity);
		    	
		    }

    		try {
    			jsonresp.put("status", "valid");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	    
	    
	     resp.setContentType("application/json");
		 printout.print(jsonresp);
	     printout.flush();
	}
}
