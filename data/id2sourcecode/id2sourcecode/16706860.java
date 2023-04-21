    private void openSignature(String digestAlg, String encryptionAlg, boolean digestOnToken) throws InvalidKeyException, SignatureException, NoSuchProviderException, NoSuchAlgorithmException, IOException, CMSException, CertificateException {
        this.msg = new CMSProcessableByteArray(dataArea.getText().getBytes("UTF8"));
        this.cmsGenerator = new ExternalSignatureCMSSignedDataGenerator();
        this.signersCertList = new ArrayList();
        log.println("Certificate bytes:\n" + formatAsHexString(getCertificate()));
        java.security.cert.X509Certificate javaCert = getJavaCertificate();
        this.signerInfoGenerator = new ExternalSignatureSignerInfoGenerator(digestAlg, encryptionAlg);
        this.signerInfoGenerator.setCertificate(javaCert);
        this.signersCertList.add(javaCert);
        this.bytesToSign = this.signerInfoGenerator.getBytesToSign(PKCSObjectIdentifiers.data, msg, "BC");
        if (!digestOnToken) {
            log.println("\nCalculating digest ...\n");
            MessageDigest md = MessageDigest.getInstance(digestAlg);
            md.update(bytesToSign);
            byte[] rawDigest = md.digest();
            log.println("Encapsulating in a DigestInfo...");
            byte[] dInfoBytes = encapsulateInDigestInfo(digestAlg, rawDigest);
            log.println("Adding Pkcs1 padding...");
            byte[] paddedBytes = applyPkcs1Padding(128, dInfoBytes);
            log.println("Encapsulated digest:\n" + formatAsHexString(dInfoBytes));
            log.println("Done.");
            setEncodedDigest(encodeFromBytes(dInfoBytes));
        }
    }
