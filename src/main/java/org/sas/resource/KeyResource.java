package org.sas.resource;

import java.io.IOException;

import org.sas.service.KeyService;

import jakarta.inject.Inject;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/key")
public class KeyResource {

    @Inject
    KeyService service;

    @GET
    @Path("/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getKeyPublic(@PathParam("username") String username) throws IOException {
        var key = service.getKeyPub(username);
        System.out.println(key);
        return Response.ok(key).build();
    }
}
