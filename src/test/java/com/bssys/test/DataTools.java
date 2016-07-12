package com.bssys.test;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.util.file.FilenameUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class DataTools {
  private final static String TEST_DATA_FOLDER = "jrts-web/src/test/test-data";

  private static Connection jdbcConnection = null;

  private static Connection getConnectionByJNDI() throws NamingException, SQLException {
    String DATASOURCE_CONTEXT = "jdbc/arquillian";

    Connection con = null;
    DataSource datasource;

    Context initialContext = new InitialContext();
    datasource = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);
    if (datasource != null) {
      con = datasource.getConnection();
    }
    return con;
  }

  public static void createDBStructures(String xmlFileName) throws Exception {
    boolean firstRun = jdbcConnection == null;

    if (firstRun) {
      jdbcConnection = getConnectionByJNDI();
    }

    Database database =
        DatabaseFactory.getInstance().
            findCorrectDatabaseImplementation(new JdbcConnection(jdbcConnection));

    Liquibase liquibase = new Liquibase(FilenameUtils.concat(TEST_DATA_FOLDER, xmlFileName),
        new FileSystemResourceAccessor(), database);
    if (firstRun) { // это нужно для того, чтобы удалить структуры, которые создал Eclipselink
      liquibase.dropAll();
    }
    liquibase.update("");
  }

  public static void populateData(String xmlFileName) throws Exception {
    FileInputStream inputStream = new FileInputStream(FilenameUtils.concat(TEST_DATA_FOLDER, xmlFileName));
    FlatXmlDataSetBuilder xmlDataSetBuilder = new FlatXmlDataSetBuilder();
    xmlDataSetBuilder.setColumnSensing(true);
    FlatXmlDataSet xmlDataSet = xmlDataSetBuilder.build(inputStream);

    IDatabaseConnection connection;
    connection = new DatabaseConnection(getConnectionByJNDI(), "public");
    connection.getConfig().setProperty(org.dbunit.database.DatabaseConfig.PROPERTY_ESCAPE_PATTERN, "\"");
    connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());

    DatabaseOperation.CLEAN_INSERT.execute(connection, xmlDataSet);
    jdbcConnection.commit();
  }

}
