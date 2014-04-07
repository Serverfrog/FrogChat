package de.serverfrog.frogchat.services;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;


/**
 *
 * @author m-p-h_000
 */
@Stateless
@Remote(UserService.class)
public class UserServiceBean implements UserService {

    @Override
    public String hello(String name) {
        return "Hello Mister Fucktart " + name + '!';
    }

}
