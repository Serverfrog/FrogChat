package de.serverfrog.frogchatgui;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.serverfrog.frogchat.encryption.PGPFrogUtil;

/**
 *
 * @author m-p-h_000
 * @param <T>
 */
public class EncryptionHelper<T> {

    private final String password;

    private final File privateKey;

    public EncryptionHelper(String password, File privateKey) {
        this.password = password;
        this.privateKey = privateKey;
    }
   

    public T encrypt(Class<? extends T> clazz) {//, MessageWrapper wrapper
//        try {
//            JAXBContext jc = JAXBContext.newInstance(clazz);
//            Unmarshaller u = jc.createUnmarshaller();
//            String decryptedBase64 = PGPFrogUtil.getDecryptedBase64(wrapper.getEncryptedContend(), privateKey, password);
//            return (T)u.unmarshal(new StringReader(decryptedBase64));
//        } catch (JAXBException | IOException ex) {
//            Logger.getLogger(EncryptionHelper.class.getName()).log(Level.SEVERE, "BLARG!", ex);
//            throw new RuntimeException(ex);
//        }
        return null;
    }
}
