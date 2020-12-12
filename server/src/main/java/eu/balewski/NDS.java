package eu.balewski;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import java.sql.ResultSet;


public class NDS {
	private final static Logger LOGGER = Logger.getLogger(NDS.class);
	private static Connection connection = null;
	private String sgid;

	public NDS(String sgid) {
		this.sgid = sgid;
		getConnection();
	}

	public String addProperty(String name, String value, NDSOutputFormat outputFormat) {
		CallableStatement statement = null;
		String sql = "{call devtools.add_property(?,?,?,?)}";
    String result = "";
		LOGGER.debug("executing {call devtools.add_property(" + name + ", " + value + ", " + this.sgid + ")}");

		try {
			statement = getConnection().prepareCall(sql);
			statement.setString(1, name);
			statement.setString(2, value);
			statement.setString(3, this.sgid);
			statement.registerOutParameter(4, java.sql.Types.BOOLEAN);
			statement.executeUpdate();
			result = statement.getBoolean(4) + "";
			LOGGER.debug("result: " + result);
      if (outputFormat == NDSOutputFormat.XML) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator");
        result = xml + "<result>" + result + "</result>";
      } else if (outputFormat == NDSOutputFormat.JSON) {
        result = "{ \"result\": " + result + "}";
      }
			return result;
		} catch (SQLException e) {
			LOGGER.error(e, e);
		}
		return result;
	}

	public String updateProperty(String name, String value, NDSOutputFormat outputFormat) {
		CallableStatement statement = null;
		String sql = "{call devtools.update_property(?,?,?,?)}";
		LOGGER.debug("executing {call devtools.update_property(" + name + ", " + value + ", " + this.sgid + ")}");
    String result = "";
		try {
			statement = getConnection().prepareCall(sql);
			statement.setString(1, name);
			statement.setString(2, value);
			statement.setString(3, this.sgid);
			statement.registerOutParameter(4, java.sql.Types.BOOLEAN);
			statement.executeUpdate();
			result = statement.getBoolean(4) + "";
      if (outputFormat == NDSOutputFormat.XML) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator");
        result = xml + "<result>" + result + "</result>";
      } else if (outputFormat == NDSOutputFormat.JSON) {
        result = "{ \"result\": " + result + "}";
      }
			LOGGER.debug("result: " + result);
			return result;
		} catch (SQLException e) {
			LOGGER.error(e, e);
		}
		return result;
	}

	public String getProperty(String name, NDSOutputFormat outputFormat) {
		CallableStatement statement = null;
		String sql = "{call devtools.get_property(?,?,?)}";
		LOGGER.debug("executing call devtools.get_property(" + name + ", " + this.sgid + ")}");

		try {
			statement = getConnection().prepareCall(sql);
			statement.setString(1, name);
			statement.setString(2, this.sgid);
			statement.registerOutParameter(3, java.sql.Types.VARCHAR);
			statement.executeUpdate();
			String result = statement.getString(3);
      if (outputFormat == NDSOutputFormat.XML) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator");
        result = xml + "<entry>" + result + "</entry>";
      } else if (outputFormat == NDSOutputFormat.JSON) {
        result = "{ \"entry\": \"" + result + "\"}";
      } else if (outputFormat == NDSOutputFormat.HTML) {
        result = "<p>" + result + "</p>";
      }
			LOGGER.debug("result: " + result);
			return result;
		} catch (SQLException e) {
			LOGGER.error(e, e);
		}
		return null;
	}

	public String deleteProperty(String name, NDSOutputFormat outputFormat) {
		CallableStatement statement = null;
		String sql = "{call devtools.delete_property(?,?,?)}";
    String result = "";
		LOGGER.debug("executing call devtools.delete_property(" + name + ", " + this.sgid + ")}");

		try {
			statement = getConnection().prepareCall(sql);
			statement.setString(1, name);
			statement.setString(2, this.sgid);
			statement.registerOutParameter(3, java.sql.Types.BOOLEAN);
			statement.executeUpdate();
			result = statement.getBoolean(3) + "";
			LOGGER.debug("result: " + result);
      if (outputFormat == NDSOutputFormat.XML) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator");
        result = xml + "<result>" + result.toString() + "</result>";
      } else if (outputFormat == NDSOutputFormat.JSON) {
        result = "{ \"result\": " + result + "}";
      }
			return result;
		} catch (SQLException e) {
			LOGGER.error(e, e);
		}
		return result;
	}

  private String getTextList(ResultSet rs) throws SQLException {
		StringBuilder result = new StringBuilder();
		while (rs.next()) {
			result.append(rs.getString(1)).append("::==::").append(rs.getTimestamp(3)).append("::==::").append(rs.getString(2)).append(System.getProperty("line.separator"));
		}
    return result.toString();
  }
    
  private String getXMLList(ResultSet rs) throws SQLException {
		StringBuilder result = new StringBuilder();
    result.append("<entries>").append(System.getProperty("line.separator"));
		while (rs.next()) {
      result.append("<entry>").append(System.getProperty("line.separator"));
			result.append("<updated>").append(rs.getTimestamp(3)).append("</updated><name>").append(rs.getString(1)).append("</name><value>").append(rs.getString(2)).append("</value>").append(System.getProperty("line.separator"));
      result.append("</entry>").append(System.getProperty("line.separator"));
		}
    result.append("</entries>").append(System.getProperty("line.separator"));

    return result.toString();
  }

  private String getJSONList(ResultSet rs) throws SQLException {
		StringBuilder result = new StringBuilder();
    result.append("{").append(System.getProperty("line.separator")).append("\"entries\": [");
		while (rs.next()) {
      result.append("{").append(System.getProperty("line.separator"));
			result.append("\"updated\": \"").append(rs.getTimestamp(3)).append("\",").append(System.getProperty("line.separator"));
      result.append("\"name\": \"").append(rs.getString(1)).append("\",").append(System.getProperty("line.separator"));
      result.append("\"value\": \"").append(rs.getString(2)).append("\"").append(System.getProperty("line.separator"));
      result.append("},").append(System.getProperty("line.separator"));
		}
    result.deleteCharAt(result.length() - 1);
    result.deleteCharAt(result.length() - 1);
    result.append("]").append(System.getProperty("line.separator"));
    result.append("}");

    return result.toString();
  }

  public String list(NDSOutputFormat outputFormat) {
		CallableStatement statement = null;
		String sql = "{ ? = call devtools.list_properties(?) }";
		StringBuilder result = new StringBuilder();
		LOGGER.debug("executing call devtools.list_properties(" + this.sgid + ")}");

		try {
      if (getConnection() != null) {
			  getConnection().setAutoCommit(false);
			  statement = getConnection().prepareCall(sql);
			  statement.setString(2, this.sgid);
			  statement.registerOutParameter(1, java.sql.Types.OTHER);
			  statement.execute();
			  ResultSet rs = (ResultSet)statement.getObject(1);
        if (outputFormat == NDSOutputFormat.TEXT) {
          result.append(getTextList(rs));
        } else if (outputFormat == NDSOutputFormat.XML) {
          result.append(getXMLList(rs));
        } else if (outputFormat == NDSOutputFormat.JSON) {
          result.append(getJSONList(rs));
        }
			  rs.close();
			  statement.close();
			  LOGGER.debug("result: " + result.toString());
			  getConnection().setAutoCommit(true);
      }
			return result.toString();
		} catch (SQLException e) {
			LOGGER.error(e, e);
		}

		return null;
  }

  //Methods keept for backward compatibility.
  //Further versions should use only parameterized methods list(NDSOutputFormat)
	public String list() {
    return list(NDSOutputFormat.TEXT);
	}

	private static Connection getConnection() {
		Properties dbConf = getProperties();

		try {
			if (connection == null || connection.isClosed()) {
				testJDBCDriver();
				createConnection(dbConf);
				LOGGER.debug("creating new Connection");
			}
		} catch (Exception e) {
			LOGGER.error(e, e);
		}

		return connection;
	}

	private static void testJDBCDriver() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.error("No PostgreSQL JDBC driver found.");
			LOGGER.error(e, e);
		}
	}

	private static void createConnection(Properties conf) {
		try {
			String host = conf.getProperty("HOST");
			String port = conf.getProperty("PORT");
			String login = conf.getProperty("LOGIN");
			String pass = conf.getProperty("PASS");
			String db = conf.getProperty("DB");
			System.out.println("connecting to database:");
			System.out.println("HOST: " + host);
			System.out.println("PORT: " + port);
			System.out.println("DB: " + db);
			System.out.println("LOGIN: " + login);
			System.out.println("PASS: *******");
			connection = DriverManager.getConnection("jdbc:postgresql://" + 
					host + ":" + port + "/" + db,
					login,
					pass);
		} catch (SQLException e) {
			LOGGER.error("Connection failed");
			LOGGER.error(e, e);
		}
	}

	private static Properties getProperties() {
		Properties properties = new Properties();

		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream in = loader.getResourceAsStream("db.conf");
			properties.load(in);
		} catch (Exception e) {
			LOGGER.error(e, e);
		}

		return properties;
	}

	/**
	 * get the value of sgid
	 * @return the value of sgid
	 */
	public String getSGID(){
		return this.sgid;
	}
	/**
	 * set a new value to sgid
	 * @param sgid the new value to be used
	 */
	public void setSGID(String sgid) {
		this.sgid=sgid;
	}
}
