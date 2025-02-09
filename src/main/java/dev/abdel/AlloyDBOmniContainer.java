package dev.abdel;

import com.github.dockerjava.api.command.InspectContainerResponse;
import java.io.IOException;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startable;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.GenericContainer;

public class AlloyDBOmniContainer extends GenericContainer<AlloyDBOmniContainer> {

    private String username = "postgres";
    private String password = "myP@ssw0rd";
    private boolean googlemlextensionenabled;
    private String googlemlextensionversion;

  public AlloyDBOmniContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DockerImageName.parse("google/alloydbomni"));
        withExposedPorts(5432);

        waitingFor(Wait.forLogMessage(".*Post startup helper completed.*\\s", 2));
    }

    @Override
    protected void configure() {
        withEnv("POSTGRES_PASSWORD", this.password);
    }

    public AlloyDBOmniContainer withPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo){
        super.containerIsStarted(containerInfo);
        if (this.googlemlextensionenabled){
          try {
            this.execInContainer("psql", "-u", this.username, "-c", "CREATE EXTENSION IF NOT EXISTS google_ml_integration VERSION '"+this.googlemlextensionversion+"';");
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
    }

    public String getJdbcUrl() {
        return "jdbc:postgresql://" + getHost() + ":" + getMappedPort(5432) + "/postgres";
    }

    public AlloyDBOmniContainer withGoogleMlExtension(boolean enabled, String version) {
      this.googlemlextensionversion = version;
      this.googlemlextensionenabled = enabled;
      return this;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

}