package dev.abdel;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.GenericContainer;

public class AlloyDBOmniContainer extends GenericContainer<AlloyDBOmniContainer> {


    private String username = "postgres";
    private String password = "myP@ssw0rd";

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
    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        super.containerIsStarted(containerInfo);
        System.out.println(this.getJdbcUrl());
    }

    public String getJdbcUrl() {
        return "jdbc:postgresql://" + getHost() + ":" + getMappedPort(5432) + "/postgres";
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

}