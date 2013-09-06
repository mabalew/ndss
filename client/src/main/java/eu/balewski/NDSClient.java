package eu.balewski;

import java.net.*;
import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

/**
 * @author: Mariusz Balewski
 */
public class NDSClient {

	private final static Logger LOGGER = Logger.getLogger(NDSClient.class);

	private String propertyName;
	private String propertyValue;
	private String sgid;
	private String operation;

	public NDSClient(String operation, String propertyName, String propertyValue, String sgid) {
		this(operation, propertyName, sgid);
		this.propertyValue = propertyValue;
	}

	public NDSClient(String operation, String propertyName, String sgid) {
		this.operation = operation;
		this.propertyName = propertyName;
		this.sgid = sgid;
	}

	public String call() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("operation", this.operation);
		params.put("propertyName", this.propertyName);
		params.put("propertyValue", this.propertyValue);
		params.put("sgid", this.sgid);

		URLConnection connection = null;
		try {
			connection = prepareUrl(params).openConnection();
		} catch (Exception e) {
			LOGGER.error(e, e);
		}
		LOGGER.debug("calling urlconnection: " + connection.toString());
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (Exception e) {
			LOGGER.error(e, e);
		}
		String line = null;
		StringBuilder result = new StringBuilder();
	
		try {
			while ((line = reader.readLine()) != null) {
				result.append(line).append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			LOGGER.error(e, e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				LOGGER.error(e, e);
			}
		}
		LOGGER.debug("result: " + result.toString());
		return result.toString();
	}

	private URL prepareUrl(Map<String, String> params) {
		String paramsString = prepareParams(params);
		URL result = null;
		try {
			String servletUrl = getProperties().getProperty("SERVLET_URL");
			result = new URL(servletUrl + paramsString);
			LOGGER.debug("prepared url: " + result.toString());
		} catch (MalformedURLException e) {
			LOGGER.error(e, e);
		}
		return result;
	}

	private String prepareParams(Map<String, String> params) {
		String url = "?";
		for (String key: params.keySet()) {
			url += key + "=" + params.get(key) + "&";
		}
		return url;
	}

	private static Properties getProperties() {
		Properties properties = new Properties();

		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream in = loader.getResourceAsStream("client.conf");
			properties.load(in);
		} catch (Exception e) {
			LOGGER.error(e, e);
		}
		return properties;
	}
}
