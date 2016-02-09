package com.glasses.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
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
public class AddGlasses extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	String name = null, brand = null, price = null, stock = null;
    	String ratio = null;
    	
    	Blob imageBlob = null;
    	  String about = null;
		try {

    	        ServletFileUpload upload = new ServletFileUpload();
    	        FileItemIterator iterator = upload.getItemIterator(req);
    	        while(iterator.hasNext()){


    	            FileItemStream item = iterator.next();
    	            InputStream stream = item.openStream();
    	            if(item.isFormField()){
    	            	if(item.getFieldName().equals("about")){

    	                    byte[] str = new byte[stream.available()];
    	                    stream.read(str);
    	                    about = new String(str,"UTF8");
    	                }
    	                else if(item.getFieldName().equals("name")){

    	                    byte[] str = new byte[stream.available()];
    	                    stream.read(str);
    	                    name = new String(str,"UTF8");
    	                }
    	                else if(item.getFieldName().equals("brand"))
    	                {
    	                	byte[] str = new byte[stream.available()];
    	                    stream.read(str);
    	                    brand = new String(str,"UTF8");
    	                }
    	                else if(item.getFieldName().equals("price"))
    	                {
    	                	byte[] str = new byte[stream.available()];
    	                    stream.read(str);
    	                    price = new String(str,"UTF8");
    	                }
    	                else if(item.getFieldName().equals("stocknum"))
    	                {
    	                	byte[] str = new byte[stream.available()];
    	                    stream.read(str);
    	                    stock = new String(str,"UTF8");
    	                }
    	                else if(item.getFieldName().equals("ratio"))
    	                {
    	                	byte[] str = new byte[stream.available()];
    	                    stream.read(str);
    	                    ratio = new String(str,"UTF8");
    	                }
    	            }else{
    	            	imageBlob = new Blob(IOUtils.toByteArray(stream));
    	                
    	               

    	                
    	            }
    	        }

    	    } catch (FileUploadException e) {

    	        e.printStackTrace();
    	    }

			Entity newPair = new Entity("Glasses");
           
   			newPair.setUnindexedProperty("image", imageBlob);
   			newPair.setUnindexedProperty("fname", name);
       	 	newPair.setUnindexedProperty("price", Double.parseDouble(price));
       	 	newPair.setUnindexedProperty("about", about);
       	 	newPair.setUnindexedProperty("brand", brand);
       	 newPair.setUnindexedProperty("stocknum", stock);
       	newPair.setUnindexedProperty("quantity", 10);
       	newPair.setUnindexedProperty("ratio", Double.parseDouble(ratio));
       	
       	 	datastore.put(newPair);
           
       }	
}
