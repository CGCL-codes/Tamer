    @Test
    public void testCreateSignature() throws Exception {
        KeyPair caKeyPair = PkiTestUtils.generateKeyPair();
        DateTime caNotBefore = new DateTime();
        DateTime caNotAfter = caNotBefore.plusYears(1);
        X509Certificate caCertificate = PkiTestUtils.generateCertificate(caKeyPair.getPublic(), "CN=TestCA", caNotBefore, caNotAfter, null, caKeyPair.getPrivate(), true, 0, null, null, new KeyUsage(KeyUsage.cRLSign | KeyUsage.keyCertSign));
        final X509CRL crl = PkiTestUtils.generateCrl(caCertificate, caKeyPair.getPrivate());
        KeyPair keyPair = PkiTestUtils.generateKeyPair();
        DateTime notBefore = new DateTime();
        DateTime notAfter = notBefore.plusMonths(1);
        X509Certificate certificate = PkiTestUtils.generateCertificate(keyPair.getPublic(), "CN=Test", notBefore, notAfter, caCertificate, caKeyPair.getPrivate(), false, 0, null, null, new KeyUsage(KeyUsage.nonRepudiation));
        ByteArrayOutputStream asicOutputStream = new ByteArrayOutputStream();
        ZipOutputStream asicZipOutputStream = new ZipOutputStream(asicOutputStream);
        ZipEntry fileZipEntry = new ZipEntry("file.txt");
        asicZipOutputStream.putNextEntry(fileZipEntry);
        asicZipOutputStream.write("hello world".getBytes());
        asicZipOutputStream.closeEntry();
        asicZipOutputStream.close();
        ByteArrayInputStream asicInputStream = new ByteArrayInputStream(asicOutputStream.toByteArray());
        ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
        TemporaryTestDataStorage temporaryDataStorage = new TemporaryTestDataStorage();
        IdentityDTO identity = new IdentityDTO();
        identity.name = "Cornelis";
        identity.firstName = "Frank";
        identity.male = true;
        byte[] photo = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        AddressDTO address = new AddressDTO();
        address.city = "Brussels";
        RevocationDataService revocationDataService = new RevocationDataService() {

            public RevocationData getRevocationData(List<X509Certificate> certificateChain) {
                RevocationData revocationData = new RevocationData();
                revocationData.addCRL(crl);
                return revocationData;
            }
        };
        TimeStampService timeStampService = new TimeStampService() {

            public byte[] timeStamp(byte[] data, RevocationData revocationData) throws Exception {
                return "encoded time-stamp token".getBytes();
            }
        };
        ASiCSignatureService testedInstance = new ASiCSignatureService(asicInputStream, DigestAlgo.SHA256, revocationDataService, timeStampService, temporaryDataStorage, identity, photo, resultOutputStream);
        List<X509Certificate> signingCertificateChain = new LinkedList<X509Certificate>();
        signingCertificateChain.add(certificate);
        signingCertificateChain.add(caCertificate);
        DigestInfo digestInfo = testedInstance.preSign(null, signingCertificateChain, identity, address, photo);
        assertNotNull(digestInfo);
        LOG.debug("digest info description: " + digestInfo.description);
        assertEquals("Associated Signature Container", digestInfo.description);
        LOG.debug("digest info algo: " + digestInfo.digestAlgo);
        assertEquals("SHA-256", digestInfo.digestAlgo);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        byte[] digestInfoValue = ArrayUtils.addAll(PkiTestUtils.SHA256_DIGEST_INFO_PREFIX, digestInfo.digestValue);
        byte[] signatureValue = cipher.doFinal(digestInfoValue);
        testedInstance.postSign(signatureValue, signingCertificateChain);
        File tmpFile = File.createTempFile("signed-container-", ".asice");
        byte[] asicResult = resultOutputStream.toByteArray();
        FileUtils.writeByteArrayToFile(tmpFile, asicResult);
        LOG.debug("ASiC file: " + tmpFile.getAbsolutePath());
        List<X509Certificate> signers = ASiCSignatureVerifier.verifySignatures(asicResult);
        assertNotNull(signers);
        assertEquals(1, signers.size());
        assertEquals(certificate, signers.get(0));
    }
