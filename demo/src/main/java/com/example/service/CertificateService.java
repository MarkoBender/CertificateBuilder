package com.example.service;

import com.example.model.IssuerData;
import com.example.model.SubjectData;
import com.example.model.SubjectInfo;
import com.example.repository.MyDatabase;
import com.example.util.CertificateGenerator;
import com.example.util.KeyStoreReader;
import com.example.util.KeyStoreWriter;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by Bender on 5/10/2017.
 */
@Service
public class CertificateService {

    public static final String keystoreFile = "myKeystore";
    public static final String keystorePassword = "password";
    public static final String entriesPassword = "password";


    private CertificateGenerator certificateGenerator = new CertificateGenerator();
    private KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
    private KeyStoreReader keyStoreReader = new KeyStoreReader();
    private MyDatabase myDatabase = new MyDatabase();



    public CertificateService(){Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());}

    //Given subjectInfo, generates X500Name for the subject
    private X500Name generateX500Name(SubjectInfo info){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, info.getCommonName());
        builder.addRDN(BCStyle.SURNAME, info.getSureName());
        builder.addRDN(BCStyle.GIVENNAME, info.getGivenName());
        builder.addRDN(BCStyle.O, info.getOrganisation());
        builder.addRDN(BCStyle.OU, info.getOrganisationUnit());
        builder.addRDN(BCStyle.C, info.getCounty());
        builder.addRDN(BCStyle.E, info.getEmail());
        builder.addRDN(BCStyle.UID, info.getUid());
        return builder.build();
    }

    //Generates a (2048 bits) RSA key pair
    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    Given an issuer name (organisation-organisation_unit pair), finds the serial number
    of this issuers current (valid) certificate from database (certs.txt). Than, it looks
    for an entry with this allias in the keystore (each entrys allias is its certificates serial number)
    and uses this keystore entry to read the information from the issuers certificate, and
    get issuers private key that will be used for new certificates signing.
     */
    private IssuerData generateIssuerData(String signerName){
        String serial = myDatabase.getSerialByName(signerName);
        X509Certificate signerCert = (X509Certificate) keyStoreReader.readCertificate(keystoreFile, keystorePassword, serial);
        PrivateKey signerPrivateKey = keyStoreReader.readPrivateKey(keystoreFile, keystorePassword, serial, entriesPassword);

        String x500Principal = signerCert.getSubjectX500Principal().toString();
        String[] tokens = x500Principal.split(", ");
        String uid = tokens[0].split("=")[1];
        String email = tokens[1].split("=")[1];
        String country = tokens[2].split("=")[1];
        String organisation = tokens[3].split("=")[1];
        String oUnit = tokens[4].split("=")[1];
        String givenName = tokens[5].split("=")[1];
        String sureName = tokens[6].split("=")[1];
        String commonName = tokens[7].split("=")[1];

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, commonName);
        builder.addRDN(BCStyle.SURNAME, sureName);
        builder.addRDN(BCStyle.GIVENNAME, givenName);
        builder.addRDN(BCStyle.O, organisation);
        builder.addRDN(BCStyle.OU, oUnit);
        builder.addRDN(BCStyle.C, country);
        builder.addRDN(BCStyle.E, email);
        builder.addRDN(BCStyle.UID, uid);
        X500Name x500Name = builder.build();

        IssuerData issuerData = new IssuerData(signerPrivateKey, x500Name);
        return issuerData;
    }

    /*
    Given a SubjectInfo object, makes a SubjectData and Issuer data, based
    on that info (since it is self-signed) and creates a new self-signed certificate.
    Saves it in the keystore and makes a new database(certs.txt) entry.
     */
    public boolean addSelfSigned(SubjectInfo subjectInfo){
        if(myDatabase.containsValid(subjectInfo.getOrganisation() +"-"+ subjectInfo.getOrganisationUnit()))
            return false;
        X500Name x500Name = generateX500Name(subjectInfo);
        String serial = String.valueOf(myDatabase.getCounter());

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 2); // Certificate duration will be fixed at 2 years
        Date endDate = calendar.getTime();

        KeyPair keyPair = generateKeyPair();

        SubjectData subjectData = new SubjectData(keyPair.getPublic(), x500Name, serial, startDate, endDate);
        IssuerData issuerData = new IssuerData(keyPair.getPrivate(), x500Name);
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData, true);

        keyStoreWriter.loadKeyStore(null, keystorePassword.toCharArray());
        keyStoreWriter.write(serial, keyPair.getPrivate(), keystorePassword.toCharArray(), certificate);
        keyStoreWriter.saveKeyStore(keystoreFile, keystorePassword.toCharArray());
        myDatabase.writeNew(true,subjectInfo.getOrganisation() + "-" + subjectInfo.getOrganisationUnit());
        return true;
    }

    /*
    Given a SubjectInfo that also contains the CA choosen by the
    user to sign the certificate, it forms a new certificate,
    stores it (together with a newly generated private key) in
    the key store and makes a new entry in the database (certs.txt).
    isCa parameter specifies if the new certificate will be a certificate authority
     */
    public boolean addCertificate(SubjectInfo subjectInfo, boolean isCa){
        if(myDatabase.containsValid(subjectInfo.getOrganisation() +"-"+ subjectInfo.getOrganisationUnit()))
            return false;
        X500Name x500Name = generateX500Name(subjectInfo);
        String serial = String.valueOf(myDatabase.getCounter());

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 2); // Certificate duration will be fixed at 2 years
        Date endDate = calendar.getTime();

        KeyPair keyPair = generateKeyPair();

        IssuerData issuerData = generateIssuerData(subjectInfo.getDesiredSigner());
        SubjectData subjectData = new SubjectData(keyPair.getPublic(), x500Name, serial, startDate, endDate);

        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData, isCa);

        keyStoreWriter.loadKeyStore(null, keystorePassword.toCharArray());
        keyStoreWriter.write(serial, keyPair.getPrivate(), keystorePassword.toCharArray(), certificate);
        keyStoreWriter.saveKeyStore(keystoreFile, keystorePassword.toCharArray());
        myDatabase.writeNew(true,subjectInfo.getOrganisation() + "-" + subjectInfo.getOrganisationUnit());
        return true;
    }

    /*
    Given a serial number, returns the status of a certificate
    2 - revoked
    1 - good
    0 - unknown
     */
    public int getStatus(String serialNumber){
        return myDatabase.getStatus(serialNumber);
    }

    /*
    Given a certificates serial number, returns that certificate (if it exists)
     */
    public String requestCert(String serialNumber){
        Certificate  cert = keyStoreReader.readCertificate(keystoreFile, keystorePassword, serialNumber);
        return cert.toString();
    }

    /*
    Given a certificates serial number, withdraws a certificate.
    Returns true if the operation has succeeded.
     */
    public boolean withdrawCert(String serialNumber){
        return myDatabase.withdraw(serialNumber);
    }

    /*
    Returns a list of all Certificate authorities in a form ("organisation-organisation_unit")
     */
    public List<String> getAllCAs(){
        return myDatabase.getAllCAs();
    }

    public static void main(String[] args) throws IOException {


        SubjectInfo si = new SubjectInfo("cnnnn", "sn", "gn", "o" , "ou", "srb", "em@il.com", "123","");
        CertificateService cs = new CertificateService();
        cs.addSelfSigned(si);
        String ret = cs.requestCert("0");

        //cs.generateIssuerData("o-ou");
        //MyDatabase mdb = new MyDatabase();
        //System.out.println( mdb.getSerialByName("o-ou"));

        SubjectInfo si2 = new SubjectInfo("cnnnn2", "sn2", "gn2", "o2" , "ou2", "srb2", "em@il.com2", "1232","o-ou");
        cs.addCertificate(si2, true);
        String brt = cs.requestCert("1");

        System.out.println(brt);




    }



}
