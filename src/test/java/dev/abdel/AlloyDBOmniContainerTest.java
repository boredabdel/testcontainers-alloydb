package dev.abdel;

import org.junit.jupiter.api.Test;
import org.testcontainers.utility.DockerImageName;
import java.sql.*;
import static org.assertj.core.api.Assertions.assertThat;
public class AlloyDBOmniContainerTest {

    @Test
    void test() throws SQLException {
        

        try (AlloyDBOmniContainer alloy = new AlloyDBOmniContainer(DockerImageName.parse("google/alloydbomni:15"))) {
            alloy.start();
            Connection connection = DriverManager.getConnection(alloy.getJdbcUrl(), alloy.getUsername(), alloy.getPassword());
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1");
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            assertThat(resultSet.getInt(1)).isEqualTo(1);
        }

    }

    @Test
    void test2() throws SQLException {
        try (AlloyDBOmniContainer alloy = new AlloyDBOmniContainer(DockerImageName.parse("google/alloydbomni:15"))
                .withPassword("testpassword")) {


            alloy.start();
            Connection connection = DriverManager.getConnection(alloy.getJdbcUrl(), alloy.getUsername(), alloy.getPassword());
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1");
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            assertThat(resultSet.getInt(1)).isEqualTo(1);
            assertThat(alloy.getPassword()).isEqualTo("testpassword");
        }
    }

}