package dev.abdel;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.testcontainers.utility.DockerImageName;

public class AlloyDBOmniMLExtensions {
  @Test
  void testMlExtensions() throws SQLException {

    AlloyDBOmniContainer alloy = new AlloyDBOmniContainer(DockerImageName.parse("google/alloydbomni:15"));
    alloy.start();
    Connection connection = DriverManager.getConnection(alloy.getJdbcUrl(), alloy.getUsername(), alloy.getPassword());
    PreparedStatement preparedStatement = connection.prepareStatement("SELECT extversion FROM pg_extension WHERE extname = 'google_ml_integration';");
    preparedStatement.execute();
    ResultSet resultSet = preparedStatement.getResultSet();
    resultSet.next();
    resultSet.getString("extversion");
    System.out.println(resultSet.getString("extversion"));
    alloy.stop();
  }
}
