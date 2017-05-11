package com.example.model;

/*
    Represents the info about the subject that request a new certificate to be issued,
    Unlike model.SubjectData, this format is adequate to be a rest controller parameter.
*/
public class SubjectInfo {

    private String commonName;
    private String sureName;
    private String givenName;
    private String organisation;
    private String organisationUnit;
    private String county;
    private String email;
    private String uid;

    private String desiredSigner;

    public SubjectInfo(){}

    public SubjectInfo(String commonName, String sureName, String givenName, String organisation, String organisationUnit, String county, String email, String uid, String desiredSigner) {
        this.commonName = commonName;
        this.sureName = sureName;
        this.givenName = givenName;
        this.organisation = organisation;
        this.organisationUnit = organisationUnit;
        this.county = county;
        this.email = email;
        this.uid = uid;
        this.desiredSigner = desiredSigner;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSureName() {
        return sureName;
    }

    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getOrganisationUnit() {
        return organisationUnit;
    }

    public void setOrganisationUnit(String organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDesiredSigner() {
        return desiredSigner;
    }

    public void setDesiredSigner(String desiredSigner) {
        this.desiredSigner = desiredSigner;
    }
}
