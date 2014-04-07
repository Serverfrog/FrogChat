package de.serverfrog.frogchat.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityListeners;

import org.granite.tide.data.DataPublishListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author m-p-h_000
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners({DataPublishListener.class})
public class Message implements Comparable<Message>, Serializable {

    private User user;

    private String message;

    private Date occurence;

    @Override
    public int compareTo(Message o) {
        if ( o.getOccurence().after(occurence) ) return 1;
        if ( o.getOccurence().before(occurence) ) return -1;
        if ( o.equals(this) ) return 0;
        if ( o.getMessage().length() != message.length() )
            if ( o.getMessage().length() < message.length() )
                return -1;
            else return 1;
        return o.hashCode() - this.hashCode();
    }

}
