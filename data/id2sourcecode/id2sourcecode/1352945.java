    @Test
    public void testPreSign() throws Exception {
        URL ooxmlUrl = AbstractOOXMLSignatureServiceTest.class.getResource("/hello-world-unsigned.docx");
        assertNotNull(ooxmlUrl);
        OOXMLTestSignatureService signatureService = new OOXMLTestSignatureService(ooxmlUrl);
        KeyPair keyPair = PkiTestUtils.generateKeyPair();
        DateTime notBefore = new DateTime();
        DateTime notAfter = notBefore.plusYears(1);
        X509Certificate certificate = PkiTestUtils.generateCertificate(keyPair.getPublic(), "CN=Test", notBefore, notAfter, null, keyPair.getPrivate(), true, 0, null, null, new KeyUsage(KeyUsage.nonRepudiation));
        DigestInfo digestInfo = signatureService.preSign(null, Collections.singletonList(certificate));
        assertNotNull(digestInfo);
        LOG.debug("digest algo: " + digestInfo.digestAlgo);
        LOG.debug("digest description: " + digestInfo.description);
        assertEquals("Office OpenXML Document", digestInfo.description);
        assertNotNull(digestInfo.digestAlgo);
        assertNotNull(digestInfo.digestValue);
        TemporaryDataStorage temporaryDataStorage = signatureService.getTemporaryDataStorage();
        String preSignResult = IOUtils.toString(temporaryDataStorage.getTempInputStream());
        LOG.debug("pre-sign result: " + preSignResult);
        File tmpFile = File.createTempFile("ooxml-pre-sign-", ".xml");
        FileUtils.writeStringToFile(tmpFile, preSignResult);
        LOG.debug("tmp pre-sign file: " + tmpFile.getAbsolutePath());
    }
