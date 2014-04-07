package de.serverfrog.frogchat.rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 *
 * @author m-p-h_000
 */
@Path("/debug")
@Stateless
public class DebugRest {

    private final static Logger L = LoggerFactory.getLogger(DebugRest.class);

    @GET
    @Produces(TEXT_PLAIN)
    public String getVersion() {
        return "1.0";
    }

    @GET
    @Path("/warn/{text}")
    @Produces(TEXT_PLAIN)
    public String printWarn(@PathParam("text") String text) {
        L.warn(text);
        return "Print OK";
    }

    @GET
    @Path("/info/{text}")
    @Produces(TEXT_PLAIN)
    public String printInfo(@PathParam("text") String text) {
        L.info(text);
        return "Print OK";
    }

    @GET
    @Path("/error/{text}")
    @Produces(TEXT_PLAIN)
    public String printError(@PathParam("text") String text) {
        L.error(text);
        return "Print OK";
    }
}
