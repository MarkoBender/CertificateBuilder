package com.example.util;

import com.example.model.IssuerData;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class KeyStoreReader {

	private KeyStore keyStore;

	public KeyStoreReader() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    public void listContent(String keyStoreFile, char[] password){

        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, password);
            Enumeration<String> aliases = keyStore.aliases();

            if(!aliases.hasMoreElements()){
                System.out.println("Selected keystore is empty.");
            }else{
                System.out.println("Selected keystore contains following entries: ");
                while(aliases.hasMoreElements())
                    System.out.println(aliases.nextElement());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }




    }

	public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, password);
			Certificate cert = keyStore.getCertificate(alias);
			PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
			return new IssuerData(privKey, issuerName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
		try {
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());

			if(ks.isKeyEntry(alias)) {
				Certificate cert = ks.getCertificate(alias);
				return cert;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
		try {

			KeyStore ks = KeyStore.getInstance("JKS", "SUN");

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());

			if(ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
				return pk;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
