package de.serverfrog.frogchat.encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.util.encoders.Base64;

import static org.bouncycastle.bcpg.PublicKeyAlgorithmTags.RSA_GENERAL;

/**
 *
 * @author m-p-h_000
 */
public class PGPFrogUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final File HOME_KEY_FOLDER = new File(
            System.getProperty("user.home") + File.separatorChar + "FrogChat" + File.separatorChar + "keys");

    public static final String SECURE_KEY_PATH_SUFFIX = File.separatorChar + "sec.asc";

    public static final String PUBLIC_KEY_PATH_SUFFIX = File.separatorChar + "pub.asc";

    private static final String PROVIDER_STRING = "BC";

    private static final String ALGORYTHM_STRING = "RSA";

    /**
     * This method generate a 4069 bit {@link PGPSecretKeyRing} with the userId and password.
     * <p>
     * @param userId
     * @param password
     * @return a {@link PGPSecretKeyRing}
     */
    public static PGPSecretKeyRing generateKeys(String userId, String password) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORYTHM_STRING, PROVIDER_STRING);
            kpg.initialize(4096);
            Date now = new Date();
            KeyPair keyPair = kpg.generateKeyPair();
            PGPKeyPair secretKey = new PGPKeyPair(RSA_GENERAL, keyPair, now);

            KeyPair keyPair2 = kpg.generateKeyPair();
            PGPKeyPair secretKey2 = new PGPKeyPair(RSA_GENERAL, keyPair2, now);

            PGPSignatureSubpacketGenerator subpacketGen = new PGPSignatureSubpacketGenerator();
            subpacketGen.setKeyFlags(true, KeyFlags.CERTIFY_OTHER | KeyFlags.SIGN_DATA
                    | KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);
            PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION,
                    secretKey, userId, RSA_GENERAL, password.toCharArray(), true, subpacketGen.generate(),
                    null, new SecureRandom(), PROVIDER_STRING);
            keyRingGen.addSubKey(secretKey2);

            return keyRingGen.generateSecretKeyRing();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | PGPException ex) {
            Logger.getLogger(PGPFrogUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * This method writes a {@link PGPSecretKeyRing} into a keyFolder
     * <p>
     * @param secretKeyRing
     * @param keyFolder
     */
    public static void writeKeysInto(PGPSecretKeyRing secretKeyRing, File keyFolder) {
        try {
            System.out.println("Read Public key");
            secretKeyRing.getPublicKey().encode(new FileOutputStream(keyFolder.getAbsolutePath() + PUBLIC_KEY_PATH_SUFFIX));
            System.out.println("Read Secrete key");
            secretKeyRing.getSecretKey().encode(new FileOutputStream(keyFolder.getAbsolutePath() + SECURE_KEY_PATH_SUFFIX));
        } catch (IOException ex) {
            Logger.getLogger(PGPFrogUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method writes a {@link PGPSecretKeyRing} into the default key folder.
     * <p>
     * @param secretKeyRing
     */
    public static void writeKeysIntoHome(PGPSecretKeyRing secretKeyRing) {
        if ( !HOME_KEY_FOLDER.exists() || !HOME_KEY_FOLDER.canWrite() ) throw new UnsupportedOperationException("Folder must exist and writeable");
        HOME_KEY_FOLDER.mkdirs();
        writeKeysInto(secretKeyRing, HOME_KEY_FOLDER);

    }

    /**
     * This method reads a {@link PGPSecretKeyRing} from a keyFolder.
     * <p>
     * @param keyFolder
     * @return
     */
    public static PGPSecretKeyRing readKeysFrom(File keyFolder) {
        try {
            InputStream decoderStream = PGPUtil.getDecoderStream(new FileInputStream(keyFolder.getAbsolutePath() + SECURE_KEY_PATH_SUFFIX));
            PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(decoderStream);
            Object nextObject = pgpObjectFactory.nextObject();
            if ( !(nextObject instanceof PGPSecretKeyRing) ) throw new UnsupportedOperationException("Readed Object was not a instance of PGPSecretKeyRing");
            return (PGPSecretKeyRing)nextObject;
        } catch (IOException ex) {
            Logger.getLogger(PGPFrogUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * This method reads a {@link PGPSecretKeyRing} from the defaul folder.
     * <p>
     * @return
     */
    public static PGPSecretKeyRing readKeysFromHome() {
        if ( !HOME_KEY_FOLDER.exists() || !HOME_KEY_FOLDER.canRead() ) throw new UnsupportedOperationException("Folder must exist and readable");
        return readKeysFrom(HOME_KEY_FOLDER);
    }

    /**
     * This method encrypt the given file with the given {@link PGPPublicKey} with {@link PGPCompressedDataGenerator#BZIP2}
     * <p>
     * @param fileName
     * @param out
     * @param encKey
     * @throws IOException
     * @throws NoSuchProviderException
     * @throws PGPException
     */
    private static void encrypt(File file, OutputStream out, PGPPublicKey encKey)
            throws IOException, NoSuchProviderException, PGPException {
        out = new DataOutputStream(out);
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        // get the data from the original file
        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedDataGenerator.BZIP2);
        PGPUtil.writeFileToLiteralData(comData.open(bOut), PGPLiteralData.BINARY, file);
        comData.close();
        // object that encrypts the data
        PGPEncryptedDataGenerator cPk = new PGPEncryptedDataGenerator(PGPEncryptedDataGenerator.CAST5,
                new SecureRandom(), PROVIDER_STRING);
        cPk.addMethod(encKey);
        // take the outputstream of the original file and turn it into a byte
        // array
        byte[] bytes = bOut.toByteArray();
        // write the plain text bytes to the armored outputstream
        OutputStream cOut = cPk.open(out, bytes.length);
        cOut.write(bytes);
        cPk.close();
        out.close();
    }

    private static String decrypt(byte[] encdata, File privateKey,
                                  char[] passwd) throws PGPException, IOException, NoSuchProviderException {
        InputStream in = PGPUtil.getDecoderStream(new ByteArrayInputStream(encdata));

        PGPObjectFactory pgpF = new PGPObjectFactory(in);
        PGPEncryptedDataList enc;
        Object o = pgpF.nextObject();
        //
        // the first object might be a PGP marker packet.
        //
        if ( o instanceof PGPEncryptedDataList ) {
            enc = (PGPEncryptedDataList)o;
        } else {
            enc = (PGPEncryptedDataList)pgpF.nextObject();
        }
        //
        // find the secret key
        //
        Iterator it = enc.getEncryptedDataObjects();
        PGPPrivateKey sKey = null;
        PGPPublicKeyEncryptedData pbe = null;
        while (sKey == null && it.hasNext()) {
            pbe = (PGPPublicKeyEncryptedData)it.next();
            sKey = findSecretKey(new FileInputStream(privateKey), pbe.getKeyID(), passwd);
        }
        if ( sKey == null ) {
            throw new IllegalArgumentException("secret key for message not found.");
        }
        InputStream clear = pbe.getDataStream(sKey, PROVIDER_STRING);
        PGPObjectFactory plainFact = new PGPObjectFactory(clear);
        Object message = plainFact.nextObject();
        if ( message instanceof PGPCompressedData ) {
            PGPCompressedData cData = (PGPCompressedData)message;
            PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream());
            message = pgpFact.nextObject();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if ( message instanceof PGPLiteralData ) {
            PGPLiteralData ld = (PGPLiteralData)message;
            InputStream unc = ld.getInputStream();
            IOUtils.copy(in, baos);
            int ch;
            while ((ch = unc.read()) >= 0) {
                baos.write(ch);
            }
        } else if ( message instanceof PGPOnePassSignatureList ) {
            throw new PGPException("encrypted message contains a signed message - not literal data.");
        } else {
            throw new PGPException("message is not a simple encrypted file - type unknown.");
        }
        if ( pbe.isIntegrityProtected() ) {
            if ( !pbe.verify() ) {
                throw new RuntimeException("Verify of Integrity is failed");
            }
        }
        return baos.toString();

    }

    public static PGPPrivateKey getPrivateKey(File privateKey, char[] pass, Iterator it) {
        PGPPrivateKey sKey = null;
        try {
            while (sKey == null && it.hasNext()) {
                PGPPublicKeyEncryptedData pbe = (PGPPublicKeyEncryptedData)it.next();
                PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(new FileInputStream(privateKey)));
                PGPSecretKey pgpSecKey = pgpSec.getSecretKey(pbe.getKeyID());
                if ( pgpSecKey == null ) {
                    return null;
                }
                sKey = pgpSecKey.extractPrivateKey(pass, PROVIDER_STRING);
            }
            if ( sKey == null ) {
                throw new IllegalArgumentException("secret key for message not found.");
            }
        } catch (PGPException | IOException | NoSuchProviderException ex) {
            Logger.getLogger(PGPFrogUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sKey;
    }
    private static PGPPrivateKey findSecretKey(InputStream keyIn, long keyID, char[] pass)
            throws IOException, PGPException, NoSuchProviderException {
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyIn));
        PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);
        if ( pgpSecKey == null ) {
            return null;
        }
        return pgpSecKey.extractPrivateKey(pass, PROVIDER_STRING);
    }

    public static String getEncryptedBase64(String inputString, PGPPublicKey key) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("FrogChat", "encryption");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.write(inputString, new FileOutputStream(tempFile));
            encrypt(tempFile, byteArrayOutputStream, key);
            return new String(Base64.decode(byteArrayOutputStream.toByteArray()));
        } catch (IOException | NoSuchProviderException | PGPException ex) {
            throw new RuntimeException("A Exception occure.ExClass=" + ex.getClass() + ", ex=" + ex.getMessage());
        } finally {
            if ( tempFile != null ) tempFile.delete();
        }

    }


    public static String getDecryptedBase64(String inputString, File privateKey, String password) throws IOException {
        try {
            return decrypt(Base64.decode(inputString), privateKey, password.toCharArray());
        } catch (PGPException | IOException | NoSuchProviderException ex) {
            throw new IOException("A Exception occure. ex=" + ex.getMessage());
        }
    }
}
