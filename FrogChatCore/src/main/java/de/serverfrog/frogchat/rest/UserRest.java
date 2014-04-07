package de.serverfrog.frogchat.rest;

import java.io.IOException;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.serverfrog.frogchat.entities.User;
import de.serverfrog.frogchat.operation.ChatSecurity;
import de.serverfrog.frogchat.operation.UserListBean;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 *
 * @author m-p-h_000
 */
@Path("/user")
@Produces(APPLICATION_XML)
@Consumes(APPLICATION_XML)
@Stateless
public class UserRest {

    private final static Logger L = LoggerFactory.getLogger(UserRest.class);

    @Inject
    private UserListBean list;

    @Inject
    private ChatSecurity chatSecurity;

    @POST
    @Path("/login")
    public String login(User user) throws IOException {
        list.login(user);
        return new String(Base64.encode(chatSecurity.getPublicKey().getEncoded()));

    }

    @POST
    @Path("/logout")
    public Response logout(User user) {
        list.logout(user);
        return Response.ok().build();
    }
//
//    @POST
//    @Path("/online/{till}")
//    public MessageWrapper getOnlineUsers(User user, @PathParam("till") long till) {
//        try {
//            if ( !list.isLoggedIn(user) ) return null;
//            list.getOnline(new Date(till));
//            String encrypt = "invalid";
//            encrypt = chatSecurity.encrypt(list.getOnline(new Date(till)), chatSecurity.getPublicKey(user.getPublicKey()));
//
//            return new MessageWrapper(encrypt);
//        } catch (Exception e) {
//            System.out.println("exxy");
//            L.warn("Error by encrpyt data:", e);
//            e.printStackTrace();
//        }
//        return null;
//    }

    @POST
    @Path("/confirmOnline")
    public Response confirmOnline(User user) {
        list.confirmOnline(user);
        return Response.ok().build();
    }
}
