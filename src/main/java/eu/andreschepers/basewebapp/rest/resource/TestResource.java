package eu.andreschepers.basewebapp.rest.resource;

import eu.andreschepers.basewebapp.rest.annotation.Secure;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
@Service
public class TestResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTestString() {
        return "{\"textValue\": \"Test String\"}";
    }

    @GET
    @Path("/secure")
    @Secure
    @Produces(MediaType.APPLICATION_JSON)
    public String getTestStringSecured() {
        return "{\"textValue\": \"Test String from secured resource\"}";
    }
}