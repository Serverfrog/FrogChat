package de.serverfrog.frogchat.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.NavigableSet;

import javax.persistence.Basic;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.granite.tide.data.DataPublishListener;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author m-p-h_000
 */
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@EntityListeners({DataPublishListener.class})
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @NotNull(message = "You must provide a date when it was Checked")
    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private Date checkedAt;

    @Setter
    private NavigableSet<Message> messages;

    public Chat(Date checkedAt, NavigableSet<Message> messages) {
        this.checkedAt = checkedAt;
        this.messages = messages;
    }

}
