package de.serverfrog.frogchat.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.DependsOn;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.serverfrog.frogchat.entities.User;
import de.serverfrog.frogchat.entities.UserList;

/**
 *
 * @author m-p-h_000
 */
@Singleton
@Startup
@DependsOn(value = "ChatSecurity")
public class UserListBean {

    private final static Logger L = LoggerFactory.getLogger(UserListBean.class);

    /**
     * Date = last confirmOnline
     * User = user
     */
    private final Map<User, Date> userlist = new ConcurrentHashMap<>();

    private Date lastChanged = new Date();

    @Schedule(hour = "*", minute = "*", second = "*/30")
    public void checkUsers() {
        Date mustLastSeen = DateUtils.addSeconds(new Date(), -30);
        for (User user : userlist.keySet()) {
            if ( userlist.get(user).before(mustLastSeen) ) {
                L.info("User {} automaticly logged out.", user.getUsername());
                userlist.remove(user);
                lastChanged = new Date();
            }
        }
    }

    public UserList getOnline(Date till) {
        try {
            System.out.println("Exception?");
            System.out.println("Online: " + userlist.keySet());
            if ( till.before(lastChanged) ) return new UserList(new ArrayList<User>());
            return new UserList(new ArrayList<>(userlist.keySet()));
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            e.printStackTrace(System.out);
            throw new RuntimeException(e);
        }
    }

    public boolean isLoggedIn(User user) {
        try {

            L.info("is user loggedIn? {}", user);
            if ( user == null ) return false;
            return userlist.containsKey(user);
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            e.printStackTrace(System.out);
            throw new RuntimeException(e);
        }
    }

    public User login(User user) {
        userlist.put(user, new Date());
        lastChanged = new Date();
        L.info("User Logged In {}", user);
        return user;
    }

    public void logout(User user) {
        L.info("User Logged out {}", user);
        lastChanged = new Date();
        userlist.remove(user);
    }

    public void confirmOnline(User user) {
        userlist.put(user, new Date());
    }

}
