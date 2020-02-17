package com.ProductFinder.Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.ProductFinder.JDO.PMF;
import com.ProductFinder.dataStructure.Store;

/**
 * Creates a new store, and returns the ID so the store may be used by an android client device
 * @author adam knox
 *
 */
@SuppressWarnings("serial")
public class CreateStore extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Store store = new Store();
        pm.makePersistent(store);
        pm.close();
		
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<head><title>Product Finder</title></head>");
	    out.println("<body>");
	    out.println("<p>A new store was created. The ID is: " + store.getID() + "</p>");
	    out.println("</body></html>");
	    out.close();
	}
}