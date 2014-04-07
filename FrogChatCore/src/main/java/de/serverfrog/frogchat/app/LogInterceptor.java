
package de.serverfrog.frogchat.app;

import org.granite.messaging.amf.process.AMF3MessageInterceptor;

import flex.messaging.messages.Message;

/**
 *
 * @author m-p-h_000
 */
public class LogInterceptor implements AMF3MessageInterceptor{

    @Override
    public void before(Message request) {
        System.out.println("BEFORE:  " + request);
    }

    @Override
    public void after(Message request, Message response) {
        System.out.println("AFTER:  " + response);
    }
    
}
