package de.serverfrog.frogchat.rest;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import de.serverfrog.frogchat.entities.Chat;
import de.serverfrog.frogchat.entities.Message;
import de.serverfrog.frogchat.operation.ChatLog;
import de.serverfrog.frogchat.operation.UserListBean;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 *
 * @author m-p-h_000
 */
@Path("/chat")
@Stateless
@Produces(APPLICATION_XML)
@Consumes(APPLICATION_XML)
public class ChatRest {

    @Inject
    private ChatLog chatLog;

    @Inject
    private UserListBean userList;

    @GET
    public Response ping() {
        return Response.ok().build();
    }

    @POST
    @Path("/send")
    public Response sendMessage(Message message) {
        if ( message == null ) return Response.serverError().build();
        System.out.println("Message:" + message);
        if ( !userList.isLoggedIn(message.getUser()) ) {
            System.out.println("Forbidden");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        chatLog.addMessage(message);
        return Response.ok().build();
    }

    @GET
    @Path("/get/{till}")
    public Chat get(@PathParam("till") long till) {
        return chatLog.getMessagesTill(new Date(till));
    }

    @GET
    @Path("/all")
    public Chat getAll() {
        return chatLog.getAllMessages();
    }
}
