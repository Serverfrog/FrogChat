package de.serverfrog.frogchat.services;

import org.granite.messaging.service.annotations.RemoteDestination;
import org.granite.tide.data.DataEnabled;

/**
 *
 * @author m-p-h_000
 */
@RemoteDestination // <1>
@DataEnabled(topic = "userTopic", publish = DataEnabled.PublishMode.ON_SUCCESS)
public interface UserService {

    public String hello(String name);

}
