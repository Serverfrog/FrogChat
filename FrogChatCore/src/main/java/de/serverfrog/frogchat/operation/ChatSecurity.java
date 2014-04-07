package de.serverfrog.frogchat.operation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPUtil;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import de.serverfrog.frogchat.ApplicationConfig;
import de.serverfrog.frogchat.encryption.PGPFrogUtil;
import de.serverfrog.frogchat.services.UserService;

/**
 *
 * @author m-p-h_000
 */
@Singleton
@Startup
public class ChatSecurity {

    private static final String USER = "server@serverfrog.de";

    @Inject
    private ApplicationConfig config;

    @Inject
    UserService service;

    private final static Logger L = LoggerFactory.getLogger(ChatSecurity.class);

    private final File PGP_PRIVATE_KEY_FILE = new File(PGPFrogUtil.HOME_KEY_FOLDER.getAbsolutePath()
            + File.separatorChar + USER + PGPFrogUtil.SECURE_KEY_PATH_SUFFIX);

    private PGPSecretKeyRing keyRing;

    @PostConstruct
    public void init() {
        System.out.println(service.hello(USER));
        File userPath = new File(PGPFrogUtil.HOME_KEY_FOLDER.getAbsolutePath() + File.separatorChar + USER);
        if ( !userPath.exists() ) userPath.mkdirs();
        if ( !userPath.canRead() || !userPath.canWrite() ) {
            L.error("Path is not readable or writeabel: {}", userPath.getAbsolutePath());
            return;
        }
        File secKey = new File(userPath.getAbsolutePath() + PGPFrogUtil.SECURE_KEY_PATH_SUFFIX);
        if ( !secKey.exists() ) {
            L.warn("Key is not exesting. Generating....");
            PGPSecretKeyRing generateKeys = PGPFrogUtil.generateKeys(USER, config.getPassword());
            PGPFrogUtil.writeKeysInto(generateKeys, userPath);
            L.warn("Key generated!");
        }

        PGPSecretKeyRing readKeysFrom = PGPFrogUtil.readKeysFrom(userPath);
        UUID randomUUID = UUID.randomUUID();
        String encryptedBase64 = PGPFrogUtil.getEncryptedBase64(randomUUID.toString(), readKeysFrom.getPublicKey());
        String decryptedBase64;

        try {
            decryptedBase64 = PGPFrogUtil.getDecryptedBase64(encryptedBase64, secKey, config.getPassword());
        } catch (IOException ex) {
            L.warn("Execption thrown till decrypting.", ex);
            throw new RuntimeException(ex);
        }

        if ( !decryptedBase64.equals(randomUUID.toString()) ) {
            return;
        }
        keyRing = readKeysFrom;

    }

    public PGPPublicKey getPublicKey() {
        return keyRing.getPublicKey();
    }

    public String encrypt(Object object, PGPPublicKey key) {
        try {
            JAXBContext jc = JAXBContext.newInstance(object.getClass());
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            m.marshal(object, sw);
            return PGPFrogUtil.getEncryptedBase64(sw.toString(), key);
        } catch (JAXBException ex) {
            L.warn("Exception thrown till encrypting", ex);
            throw new RuntimeException(ex);
        }
    }

    public Object decrypt(String encryptedMessage, PGPPublicKey key, Class clazz) {
        try {
            String decryptedBase64 = PGPFrogUtil.getDecryptedBase64(encryptedMessage, PGP_PRIVATE_KEY_FILE, config.getPassword());

            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller m = jc.createUnmarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            return m.unmarshal(new StringReader(decryptedBase64));
        } catch (IOException | JAXBException ex) {
            L.warn("Exception thrown till decrypting", ex);
            throw new RuntimeException(ex);
        }
    }

    public PGPPublicKey getPublicKey(byte[] publicKey) {
        try {
            InputStream decoderStream = PGPUtil.getDecoderStream(new ByteArrayInputStream(publicKey));
            PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(decoderStream);
            Object nextObject = pgpObjectFactory.nextObject();
            if ( !(nextObject instanceof PGPPublicKeyRing) ) {
                throw new SecurityException("PublicKey was not obtainable. Not a Insatce of PGPPublicKeyRing");
            }
            return ((PGPPublicKeyRing)nextObject).getPublicKey();
        } catch (IOException ex) {
            L.warn("PublicKey was not obtainable", ex);
            throw new SecurityException("PublicKey was not obtainable", ex);
        }
    }

}
