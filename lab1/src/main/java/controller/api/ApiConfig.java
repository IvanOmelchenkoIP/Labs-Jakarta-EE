package controller.api;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class ApiConfig extends Application {

	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(VotingListApi.class);
        classes.add(VotingHostApi.class);
        classes.add(VotingDetailsApi.class);
        classes.add(VotingVoteApi.class);
        return classes;
    }
}
