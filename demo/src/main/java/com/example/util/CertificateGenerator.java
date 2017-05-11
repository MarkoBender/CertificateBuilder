package com.example.util;

import com.example.model.IssuerData;
import com.example.model.SubjectData;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

public class CertificateGenerator {
	public CertificateGenerator() {}

	public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, boolean isCa) {
		try {
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
			builder = builder.setProvider("BC");

			ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
					new BigInteger(subjectData.getSerialNumber()),
					subjectData.getStartDate(),
					subjectData.getEndDate(),
					subjectData.getX500name(),
					subjectData.getPublicKey());

			X509CertificateHolder certHolder = certGen.build(contentSigner);


			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");
			return certConverter.getCertificate(certHolder);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
