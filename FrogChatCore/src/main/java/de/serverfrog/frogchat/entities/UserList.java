package de.serverfrog.frogchat.entities;

import java.io.Serializable;
import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({DataPublishListener.class})
public class UserList implements Serializable {

    private List<User> users;
}
