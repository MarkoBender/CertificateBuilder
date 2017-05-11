package com.example.model;

import com.example.model.SubjectInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.PublicKey;
import java.util.Date;

public class SubjectData {

	private PublicKey publicKey;
	private X500Name x500name;
	private String serialNumber;
	private Date startDate;
	private Date endDate;

	public SubjectData() {

	}

	public SubjectData(PublicKey publicKey, X500Name x500name, String serialNumber, Date startDate, Date endDate) {
		this.publicKey = publicKey;
		this.x500name = x500name;
		this.serialNumber = serialNumber;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/*
	public SubjectData(SubjectInfo info){

		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, info.getCommonName());
		builder.addRDN(BCStyle.SURNAME, info.getSureName());
		builder.addRDN(BCStyle.GIVENNAME, info.getGivenName());
		builder.addRDN(BCStyle.O, info.getOrganisation());
		builder.addRDN(BCStyle.OU, info.getOrganisationUnit());
		builder.addRDN(BCStyle.C, info.getCounty());
		builder.addRDN(BCStyle.E, info.getEmail());
		builder.addRDN(BCStyle.UID, info.getUid());
		this.x500name = builder.build();


		Date currDate = new Date();
		this.startDate = currDate;
		currDate.setTime(currDate.getTime()+ 1000*60*25*365*2); // plus 2 years
		this.endDate = currDate;
	}
	*/

	public X500Name getX500name() {
		return x500name;
	}

	public void setX500name(X500Name x500name) {
		this.x500name = x500name;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
