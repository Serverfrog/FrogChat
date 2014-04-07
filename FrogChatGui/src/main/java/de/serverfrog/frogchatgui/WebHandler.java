package de.serverfrog.frogchatgui;

import java.io.File;
import java.util.Date;

//import javax.ws.rs.core.Response;

import org.apache.commons.lang3.time.DateUtils;
//import org.apache.cxf.jaxrs.client.WebClient;
import org.bouncycastle.openpgp.PGPKeyRing;
import org.bouncycastle.openpgp.PGPPublicKey;

import de.serverfrog.frogchat.entities.Chat;
import de.serverfrog.frogchat.entities.User;
import de.serverfrog.frogchat.entities.UserList;



/**
 *
 * @author m-p-h_000
 */
public class WebHandler {

    private final User user;

    private long lastChatCheck;

    private long lastUserCheck;

    private final PGPKeyRing keyRing;

    private final File privateFile;

    private final PGPPublicKey severKey;

    private final String server;

    private final String password;

    private WebHandler(String server, User user, PGPKeyRing keyRing, PGPPublicKey severKey, String password, File privateFile) {
        this.user = user;
        this.keyRing = keyRing;
        this.severKey = severKey;
        this.server = server;
        this.password = password;
        this.privateFile = privateFile;
        Date date = new Date();
        DateUtils.addHours(date, -1);
        lastChatCheck = date.getTime();
        lastUserCheck = date.getTime();
    }

    private static WebHandler webHandler;

    public static WebHandler getInstance(String server, User user, PGPKeyRing keyRing, PGPPublicKey severKey, String password, File privateFile) {
        if ( webHandler == null ) {
            webHandler = new WebHandler(server, user, keyRing, severKey, password, privateFile);
        }
        return webHandler;
    }

    public void confirmUser() {
//        WebClient webclient = getWebclient();
//        webclient.path("user").path("confirmOnline").post(user);
    }

    public void logout() {
//        WebClient webclient = getWebclient();
//        webclient.path("user").path("logout").post(user);
    }

    public Chat getChat() {
//        WebClient webclient = getWebclient();
//        long toChecked = lastChatCheck;
//        lastChatCheck = new Date().getTime();
//        Chat messages = webclient.path("chat").path("get").path(toChecked).get(Chat.class);
//        confirmUser();
//        return messages;
        return null;
    }

    public void sendMessage(String msg) {
//        WebClient webclient = getWebclient();
//        try {
//            Message message = new Message(user, msg, new Date());
//            webclient.path("chat").path("send").post(message);
//        } catch (Exception e) {
//            e.printStackTrace(System.out);
//        }
    }

    public UserList getOnlineUser() {
        UserList users = null;
        try {
//            WebClient webclient = getWebclient();
//            long toChecked = lastUserCheck;
//            lastUserCheck = new Date().getTime();
//            WebClient path = webclient.path("user").path("online").path(toChecked);
//            System.out.println("path:" + path.getCurrentURI());
//            Response poster = path.post(user);
//            BufferedInputStream bufferedInputStream = new BufferedInputStream((InputStream)poster.getEntity());
//            byte[] buffer = new byte[poster.getLength()];
//            
//            IOUtils.readFully(bufferedInputStream, buffer);
//            System.out.println(new String(buffer));
//            
//            
//            MessageWrapper post = path.post(user, MessageWrapper.class);
//            EncryptionHelper<UserList> encryptionHelper = new EncryptionHelper<>(password, privateFile);
//            users = encryptionHelper.encrypt(UserList.class, post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

//    private WebClient getWebclient() {
//        return WebClient.create(server + "/FrogChatWeb/rest/");
//    }

}
