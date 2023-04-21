    public static void checkSigningCertificate(X509Certificate signingCertificate, SignedSignaturePropertiesType signedSignatureProperties) throws XAdESValidationException, CertificateEncodingException {
        CertIDListType signingCertificateCertIDList = signedSignatureProperties.getSigningCertificate();
        List<CertIDType> signingCertificateCertIDs = signingCertificateCertIDList.getCert();
        CertIDType signingCertificateCertID = signingCertificateCertIDs.get(0);
        DigestAlgAndValueType signingCertificateDigestAlgAndValue = signingCertificateCertID.getCertDigest();
        String certXmlDigestAlgo = signingCertificateDigestAlgAndValue.getDigestMethod().getAlgorithm();
        String certDigestAlgo = XAdESUtils.getDigestAlgo(certXmlDigestAlgo);
        byte[] certDigestValue = signingCertificateDigestAlgAndValue.getDigestValue();
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(certDigestAlgo);
        } catch (NoSuchAlgorithmException e) {
            throw new XAdESValidationException("message digest algo error: " + e.getMessage(), e);
        }
        byte[] actualCertDigestValue = messageDigest.digest(signingCertificate.getEncoded());
        if (!Arrays.equals(actualCertDigestValue, certDigestValue)) {
            throw new XAdESValidationException("XAdES signing certificate not corresponding with actual signing certificate");
        }
        X509IssuerSerialType issuerSerial = signingCertificateCertID.getIssuerSerial();
        BigInteger serialNumber = issuerSerial.getX509SerialNumber();
        if (false == signingCertificate.getSerialNumber().equals(serialNumber)) {
            throw new XAdESValidationException("xades:SigningCertificate serial number mismatch");
        }
        X509Name issuerName;
        try {
            issuerName = new X509Name((ASN1Sequence) new ASN1InputStream(signingCertificate.getIssuerX500Principal().getEncoded()).readObject());
        } catch (IOException e) {
            throw new XAdESValidationException("error parsing xades:SigningCertificate ds:X509IssuerName: " + e);
        }
        X509Name xadesIssuerName = new X509Name(issuerSerial.getX509IssuerName());
        if (false == issuerName.equals(xadesIssuerName)) {
            throw new XAdESValidationException("xades:SigningCertificate issuer name mismatch");
        }
        LOG.debug("XAdES SigningCertificate OK");
    }
