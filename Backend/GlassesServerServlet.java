package com.glasses.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.Random;

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
import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("serial")
public class GlassesServerServlet extends HttpServlet {
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
		String email = req.getParameter("email");
		String name = req.getParameter("name");
		String secQuestion = req.getParameter("question");
		String secAnswer = req.getParameter("answer");
		String salt = "";
		
		JSONObject jsonresp = new JSONObject();
		PrintWriter printout = resp.getWriter();
	
		
		boolean isUser = false;
		
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    
	    Filter userFilter = new FilterPredicate("username", FilterOperator.EQUAL, user.toLowerCase());
	    Filter passwordFilter = new FilterPredicate("email", FilterOperator.EQUAL, email.toLowerCase());
	    Filter compositeFilter = CompositeFilterOperator.or(userFilter, passwordFilter);
	    
	    Query q = new Query("user").setFilter(compositeFilter);
	    PreparedQuery pq = datastore.prepare(q);
	    int size = 0;
	    for (Entity result : pq.asIterable()) 
	    {
	    	  size++;
	    	  if(result.getProperty("username").equals(user.toLowerCase()))
	    		 isUser = true;
	    }

        if(size != 0)
        {
        	if(isUser)
        	{
					try {
						jsonresp.put("reson", "username");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        	}
        	else
        	{
					try {
						jsonresp.put("reson", "email");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        	}
        }
      else
      {
        Entity appUser = new Entity("user");
        try {
			salt = getSalt();
		} catch (NoSuchAlgorithmException e1) {
			
				try {
					jsonresp.put("reson", "error");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   resp.setContentType("application/json");
				 printout.print(jsonresp.toString());
			     printout.flush();
			
			
			e1.printStackTrace();
			
		}
        appUser.setProperty("username", user.toLowerCase());
        appUser.setProperty("password", hashInfo(pass, salt));
        appUser.setProperty("email", email.toLowerCase());
        appUser.setProperty("name", name);
        appUser.setProperty("question", secQuestion);
        appUser.setProperty("answer", secAnswer);
        appUser.setProperty("salt", salt);
        appUser.setProperty("locked", "false");

        datastore.put(appUser);
		
        try {
			jsonresp.put("reson", "success");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       }
		
		
	     resp.setContentType("application/json");
		 printout.print(jsonresp.toString());
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
    
    private static String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
	}
