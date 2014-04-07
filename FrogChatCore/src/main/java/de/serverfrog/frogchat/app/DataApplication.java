package de.serverfrog.frogchat.app;

import org.granite.config.servlet3.ServerFilter;
import org.granite.gravity.config.servlet3.MessagingDestination;
import org.granite.tide.ejb.EjbConfigProvider;

/**
 *
 * @author m-p-h_000
 */
@ServerFilter(configProviderClass = EjbConfigProvider.class, amf3MessageInterceptor = LogInterceptor.class) // <1>
public class DataApplication {

    @MessagingDestination(noLocal = true, sessionSelector = true)
    public String dataTopic; // <2>
}
