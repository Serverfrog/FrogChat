package de.serverfrog.frogchat.operation;

import java.util.Date;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.serverfrog.frogchat.entities.Chat;
import de.serverfrog.frogchat.entities.Message;

/**
 *
 * @author m-p-h_000
 */
@Singleton
@Startup
@DependsOn(value = "ChatSecurity")
public class ChatLog {

    private final static Logger L = LoggerFactory.getLogger(ChatLog.class);

    private final NavigableSet<Message> messages = new ConcurrentSkipListSet<>();

    public void addMessage(Message message) {
        L.info("Add Message {}", message);
        messages.add(message);
    }

    public Chat getMessagesTill(Date till) {
        NavigableSet<Message> newMesages = new TreeSet<>();
        for (Message message : messages) {
            if ( message.getOccurence().before(till) ) break;
            newMesages.add(message);
        }
        return new Chat(till, newMesages);

    }

    public Chat getAllMessages() {
        return new Chat(new Date(), messages);
    }

}
