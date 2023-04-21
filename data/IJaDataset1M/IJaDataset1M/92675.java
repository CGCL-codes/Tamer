package com.gestioni.adoc.aps.system.services.firmaDigitale;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.AbstractService;

/**
 *  This class implements  methods necessary to apply the digital signature on all document.
 */
public class Sign extends AbstractService {

    @Override
    public void init() throws Exception {
        ApsSystemUtils.getLogger().info(this.getClass().getName() + " ready");
    }

    /**
	 * 
	 * @param store
	 * @param sPass
	 * @return keystore
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
    public static KeyStore loadKeyStore(File store, char[] sPass) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore myKS = null;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        myKS = KeyStore.getInstance("pkcs12");
        FileInputStream fis = new FileInputStream(store);
        myKS.load(fis, sPass);
        fis.close();
        return myKS;
    }

    /**
	 * 
	 * @param store
	 * @param sPass
	 * @param kPass
	 * @param alias
	 * @param ks
	 * @return private key
	 * @throws Throwable
	 */
    public PrivateKey getPrivateKey(File store, String sPass, String kPass, String alias, KeyStore ks) throws Throwable {
        try {
            Key key = null;
            PrivateKey privateKey = null;
            if (ks.containsAlias(alias)) {
                key = ks.getKey(alias, kPass.toCharArray());
                if (key instanceof PrivateKey) {
                    privateKey = (PrivateKey) key;
                    return privateKey;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getPrivateKey");
            return null;
        }
    }
}
