
package de.serverfrog.frogchat.entities;

import java.io.Serializable;

import javax.persistence.EntityListeners;

import org.granite.tide.data.DataPublishListener;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author m-p-h_000
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "publicKey")
@Getter
@Setter
@EqualsAndHashCode
@EntityListeners({DataPublishListener.class})
public class User implements Serializable{
    /**
     * The Username.
     */
    private String username;
    /**
     * The Base64 Encoded PublicKey.
     */
    private String publicKey;

}
