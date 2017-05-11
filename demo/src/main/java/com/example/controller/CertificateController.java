package com.example.controller;

import com.example.model.SubjectInfo;
import com.example.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {

    @Autowired
    CertificateService service;

    @RequestMapping(value = "/createSelfSignedCertificate" , method = RequestMethod.POST)
    public boolean createSelfSignedCertificate(@RequestBody SubjectInfo subjectInfo){
        return service.addSelfSigned(subjectInfo);
    }

    @RequestMapping(value = "/createCACertificate" , method = RequestMethod.POST)
    public boolean createCertificateAuthorityCertificate(@RequestBody SubjectInfo subjectInfo){
        return service.addCertificate(subjectInfo,true);
    }

    @RequestMapping(value = "/createOrdinaryCertificate" , method = RequestMethod.POST)
    public boolean createOrdinaryCertificate(@RequestBody SubjectInfo subjectInfo){
        return service.addCertificate(subjectInfo,false);
    }

    @RequestMapping(value = "/withdrawCertificate/{serialNumber}" , method = RequestMethod.POST)
    public boolean withdrawCertificate(@PathVariable String serialNumber){
        return service.withdrawCert(serialNumber);
    }

    @RequestMapping(value = "/requestCertificate/{serialNumber}" , method = RequestMethod.GET)
    public String requestCertificate(@PathVariable String serialNumber){
        return service.requestCert(serialNumber);
    }

    @RequestMapping(value = "/requestCertificateStatus/{serialNumber}" , method = RequestMethod.GET)
    public int requestCertificateStatus(@PathVariable String serialNumber){
        return service.getStatus(serialNumber);
    }

    @RequestMapping(value = "/requestAllCertificateAuthorities" , method = RequestMethod.GET)
    public List<String> requestAllCertificateAuthorities(){
        return service.getAllCAs();
    }


}
