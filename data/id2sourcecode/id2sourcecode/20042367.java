    public void testX509V3Authenticate() throws Exception {
        X509Certificate cert = null;
        try {
            cert = org.xmldap.util.XmldapCertsAndKeys.getXmldapCert();
        } catch (CertificateException e) {
            e.printStackTrace();
            assertFalse(e.getMessage(), true);
        }
        if (cert == null) {
            throw new Exception("oops");
        }
        String tsURL = "https://xmldap.org/sts/tokenservice";
        String mexURL = "https://xmldap.org/sts/mex";
        X509Certificate userCert = org.xmldap.util.XmldapCertsAndKeys.getXmldapCert1();
        String userCertHash = CryptoUtils.digest(userCert.getEncoded(), "SHA");
        UserCredential usercredential = new UserCredential(UserCredential.X509, userCertHash);
        TokenServiceReference tsr = new TokenServiceReference(tsURL, mexURL, cert, usercredential);
        String actual = null;
        String expected = "<ic:TokenService xmlns:ic=\"http://schemas.xmlsoap.org/ws/2005/05/identity\">" + "<wsa:EndpointReference xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">" + "<wsa:Address>https://xmldap.org/sts/tokenservice</wsa:Address><wsa:Metadata>" + "<mex:Metadata xmlns:mex=\"http://schemas.xmlsoap.org/ws/2004/09/mex\">" + "<mex:MetadataSection><mex:MetadataReference>" + "<wsa:Address>https://xmldap.org/sts/mex</wsa:Address></mex:MetadataReference>" + "</mex:MetadataSection></mex:Metadata></wsa:Metadata>" + "<wsid:Identity xmlns:wsid=\"http://schemas.xmlsoap.org/ws/2006/02/addressingidentity\">" + "<ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" + "<ds:X509Data><ds:X509Certificate>MIIDXTCCAkUCBEQd+4EwDQYJKoZIhvcNAQEEBQAwczELMAkGA1UEBhMCVVMxE" + "zARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBGcmFuY2lzY28xDzANBgNVBAoTBnhtbGRhcDERMA8GA1UECxMI" + "aW5mb2NhcmQxEzARBgNVBAMTCnhtbGRhcC5vcmcwHhcNMDYwMzIwMDA0NjU3WhcNMDYwNjE4MDA0NjU3WjBzMQswCQYDVQ" + "QGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZyYW5jaXNjbzEPMA0GA1UEChMGeG1sZGFwMREw" + "DwYDVQQLEwhpbmZvY2FyZDETMBEGA1UEAxMKeG1sZGFwLm9yZzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAN" + "MnkVA4xfpG0bLos9FOpNBjHAdFahy2cJ7FUwuXd/IShnG+5qF/z1SdPWzRxTtpFFyodtXlBUEIbiT+IbYPZF1vCcBrcFa8" + "Kz/4rBjrpPZgllgA/WSVKjnJvw8q4/tO6CQZSlRlj/ebNK9VyT1kN+MrKV1SGTqaIJ2l+7Rd05WHscwZMPdVWBbRrg76YT" + "fy6H/NlQIArNLZanPvE0Vd5QfD4ZyG2hTh3y7ZlJAUndGJ/kfZw8sKuL9QSrh4eOTc280NQUmPGz6LP5MXNmu0RxEcomod" + "1+ToKll90yEKFAUKuPYFgm9J+vYm4tzRequLy/njteRIkcfAdcAtt6PCYjUCAwEAATANBgkqhkiG9w0BAQQFAAOCAQEAUR" + "txiA7qDSq/WlUpWpfWiZ7HvveQrwTaTwV/Fk3l/I9e9WIRN51uFLuiLtZMMwR02BX7Yva1KQ/Gl999cm/0b5hptJ+TU29r" + "VPZIlI32c5vjcuSVoEda8+BRj547jlC0rNokyWm+YtBcDOwfHSPFFwVPPVxyQsVEebsiB6KazFq6iZ8A0F2HLEnpsdFnGr" + "SwBBbH3I3PH65ofrTTgj1Mjk5kA6EVaeefDCtlkX2ogIFMlcS6ruihX2mlCLUSrlPs9TH+M4j/R/LV5QWJ93/X9gsxFrxV" + "FGg3b75EKQP8MZ111/jaeKd80mUOAiTO06EtfjXZPrjPN4e2l05i2EGDUA==</ds:X509Certificate></ds:X509Data>" + "</ds:KeyInfo></wsid:Identity></wsa:EndpointReference><ic:UserCredential>" + "<ic:DisplayCredentialHint>Choose a certificate</ic:DisplayCredentialHint><ic:X509V3Credential>" + "<ds:X509Data xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" + "<wsse:KeyIdentifier " + "xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" " + "ValueType=\"http://docs.oasis-open.org/wss/2004/xx/oasis-2004xx-wss-soap-message-security-1.1#ThumbprintSHA1\" " + "EncodingType=\"http://docs.oasis-open.org/wss/2004/xx/oasis-2004xx-wss-soap-message-security-1.1#Base64Binary\">" + "v3a+zGIn245/tqOj9ab8yqhUg8k=</wsse:KeyIdentifier></ds:X509Data></ic:X509V3Credential>" + "</ic:UserCredential></ic:TokenService>";
        try {
            actual = tsr.toXML();
        } catch (SerializationException e) {
            e.printStackTrace();
            assertFalse(e.getMessage(), true);
        }
        assertEquals(expected, actual);
    }
