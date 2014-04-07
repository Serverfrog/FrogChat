package de.serverfrog.frogchat.gui;

import java.net.URISyntaxException;

import org.granite.client.tide.Context;
import org.granite.client.tide.server.ServerSession;
import org.granite.client.tide.server.TideFaultEvent;
import org.granite.client.tide.server.TideResponder;
import org.granite.client.tide.server.TideResultEvent;

import de.serverfrog.frogchat.gui.services.UserService;

/**
 *
 * @author m-p-h_000
 */
public class LoginController {


    public LoginController() throws URISyntaxException, Exception {
        Context context = MainApp.getContext();
        final ServerSession serverSession = context.set(
                new ServerSession("/chat", "localhost", 8080)); // <2>
        serverSession.addRemoteAliasPackage("org.graniteds.tutorial.data.client"); // <3>
        final UserService helloWorldService = context.set("userService",
                new UserService(serverSession)); // <3>

        serverSession.start(); // <4>
        // end::client-setup[]

        System.out.println("Kick in");

        helloWorldService.call("hello", "Megahans----------------", // <1>
                new TideResponder<String>() { // <2>
                    @Override
                    public void result(TideResultEvent<String> event) { // <3>
                        System.out.println("YEAAAAAAA:" + event.getResult());
                    }

                    @Override
                    public void fault(TideFaultEvent event) { // <4>
                        System.err.println("-----------------------------");
                        System.err.println("Fault: " + event.getFault().getCode() + ": "
                                + event.getFault().getFaultDescription());
                        System.err.println("-----------------------------");
                    }
                }
        );

    }

    public static void main(String[] args) throws URISyntaxException, Exception {
        LoginController hc = new LoginController();
    }
}
