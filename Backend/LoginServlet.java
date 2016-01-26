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
public class LoginServlet extends HttpServlet {
	Random random = new Random();
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("");
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String user = req.getParameter("user");
		String pass = req.getParameter("pass");
		String salt = "";
		
		JSONObject jsonresp = new JSONObject();
		PrintWriter printout = resp.getWriter();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    
		//Check is entity with username exists
		
	    Filter userFilter = new FilterPredicate("username", FilterOperator.EQUAL, user.toLowerCase());
	    
	    Query q = new Query("user").setFilter(userFilter);

	    PreparedQuery pq = datastore.prepare(q);
	    
	    int size = 0;
	    Entity userEntity = null;
	    
	    for (Entity result : pq.asIterable()) {
	    	  userEntity = result;
	    	}
	    
	    if(!(userEntity == null))
	    {
	    	salt = userEntity.getProperty("salt").toString();
	    	String locked = userEntity.getProperty("locked").toString();
	    	
	    	if(userEntity.getProperty("password").toString().equals(hashInfo(pass, salt)) && !locked.equals("true"))
	    	{
	    		try {
					jsonresp.put("reson", "success");
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    	}
	    	else if(locked.equals("true"))
	    	{
	    		try {
					jsonresp.put("reson", "locked");
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    	}
	    	else
	    	{
	    		try {
					jsonresp.put("reson", "none");
				} catch (JSONException e) {
					e.printStackTrace();
				}
	    	}
	    }
	    else
	    {
    		try {
				jsonresp.put("reson", "none");
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    }
	    
	    
	     resp.setContentType("application/json");
		 printout.print(jsonresp);
	     printout.flush();
	}
	

    private static String hashInfo(String info, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(info.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
