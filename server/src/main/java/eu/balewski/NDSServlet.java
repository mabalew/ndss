package eu.balewski;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class NDSServlet extends HttpServlet {
	private NDS nds = null;
	private String sgid;
	private boolean html = false;

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
		if (request.getParameter("html") != null && !request.getParameter("html").isEmpty() && "true".equals(request.getParameter("html")))
			this.html = true;

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
		String result = getNDS().addProperty(propertyName, propertyValue) + "";
		this.html = false;

		return result;
	}

	private String doUpdate(String propertyName, String propertyValue) {
		String result = getNDS().updateProperty(propertyName, propertyValue) + "";
		this.html = false;

		return result;
	}

	private String doDelete(String propertyName) {
		String result = getNDS().deleteProperty(propertyName) + "";
		this.html = false;

		return result;
	}

	private String doGet(String propertyName) {
		if (!html) {
			String result = getNDS().getProperty(propertyName);
			this.html = false;

			return result;
		} else {
			return "<p>" + getNDS().getProperty(propertyName) + "</p>";
		}
	}

	private String doList() {
		StringBuilder sb = new StringBuilder();
		if (!html)
			return getNDS().list();
		else {
			String[] lines = getNDS().list().split(System.getProperty("line.separator"));
			sb.append("<table border='1' cellpadding='0' cellspacing='0'>");	
			for (String line: lines) {
				String[] eqLine = line.split("::==::");
				String key = eqLine[0];
				String lastUpdate = eqLine[1];
				String value = eqLine[2];
				sb.append("<tr>");
				sb.append("<td>" + key + "</td>");
				sb.append("<td>" + lastUpdate + "</td>");
				sb.append("<td>" + value + "</td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
			this.html = false;

			return sb.toString();
		}
	}

	private NDS getNDS() {
		nds = new NDS(this.sgid);
		return nds;
	}
}
