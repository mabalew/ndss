package eu.balewski;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class NDSServlet extends HttpServlet {
	private NDS nds = null;
	private String sgid;
	private boolean text = false;
	private boolean html = false;
	private boolean xml = false;
	private boolean json = false;
  private NDSOutputFormat outputFormat;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGetOrPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGetOrPost(request, response);
	}

	private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String propertyName =request.getParameter("propertyName");
		String propertyValue = request.getParameter("propertyValue");
		this.sgid = request.getParameter("sgid");
		String operation = request.getParameter("operation");
		if (request.getParameter("format") != null && !request.getParameter("format").isEmpty() && "html".equals(request.getParameter("format"))) {
      response.setContentType("text/html");
      this.outputFormat = NDSOutputFormat.HTML;
			this.html = true;
    }
    if (request.getParameter("format") != null && !request.getParameter("format").isEmpty() && "text".equals(request.getParameter("format"))) {
      response.setContentType("text/plain");
      this.outputFormat = NDSOutputFormat.TEXT;
			this.text = true;
    }
    if (request.getParameter("format") != null && !request.getParameter("format").isEmpty() && "xml".equals(request.getParameter("format"))) {
      response.setContentType("text/xml; charset=UTF-8");
      this.outputFormat = NDSOutputFormat.XML;
			this.xml = true;
    }
    if (request.getParameter("format") != null && !request.getParameter("format").isEmpty() && "json".equals(request.getParameter("format"))) {
      response.setContentType("application/json");
      this.outputFormat = NDSOutputFormat.JSON;
			this.json = true;
    }

		response.getWriter().write(doOperation(operation, propertyName, propertyValue));
	}

	private String doOperation(String operation, String propertyName, String propertyValue) {
		if (operation == null || operation.isEmpty()) {
			return "null";
		} else if ("add".equalsIgnoreCase(operation)) {
			return doAdd(propertyName, propertyValue);
		} else if ("update".equalsIgnoreCase(operation)) {
			return doUpdate(propertyName, propertyValue);
		} else if ("delete".equalsIgnoreCase(operation)) {
			return doDelete(propertyName);
		} else if ("get".equalsIgnoreCase(operation)) {
			String result = doGet(propertyName);
			return (result != null ? result : "null");
		} else if ("list".equalsIgnoreCase(operation)) {
			String result = doList();
			return (result != null ? result : "null");
		} else {
			return "unknown operation";
		}
	}

	private String doAdd(String propertyName, String propertyValue) {
		String result = getNDS().addProperty(propertyName, propertyValue, this.outputFormat) + "";
		this.html = false;
		this.text = false;
		this.xml = false;
		this.json = false;

		return result;
	}

	private String doUpdate(String propertyName, String propertyValue) {
		String result = getNDS().updateProperty(propertyName, propertyValue, this.outputFormat) + "";
		this.html = false;
		this.text = false;
		this.xml = false;
		this.json = false;

		return result;
	}

	private String doDelete(String propertyName) {
		String result = getNDS().deleteProperty(propertyName, this.outputFormat) + "";
		this.html = false;
		this.text = false;
		this.xml = false;
		this.json = false;

		return result;
	}

	private String doGet(String propertyName) {
		String result = getNDS().getProperty(propertyName, this.outputFormat);
		this.xml = false;
		this.json = false;
		this.html = false;

    return result;
	}

	private String doList() {
		StringBuilder sb = new StringBuilder();
    if (text) {
		  this.text = false;
			return getNDS().list(NDSOutputFormat.TEXT);
    } else if (xml) {
		  this.xml = false;
			return getNDS().list(NDSOutputFormat.XML);
    } else if (json) {
		  this.json = false;
      return getNDS().list(NDSOutputFormat.JSON);
    } else if (html) {
			this.html = false;
			return getNDS().list(NDSOutputFormat.HTML);
		}
		return sb.toString();
	}

	private NDS getNDS() {
		nds = new NDS(this.sgid);
		return nds;
	}
}
