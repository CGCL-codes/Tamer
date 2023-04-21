    public void testGenerateCMSMessageFromResource() throws Exception {
        byte[] testData = "testData".getBytes();
        byte[] signedData = ClusterClassLoaderUtils.generateCMSMessageFromResource(testData, null, null, "BC");
        assertNotNull(signedData);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(signedData));
        assertFalse(dis.readBoolean());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (dis.available() != 0) {
            baos.write(dis.read());
        }
        assertTrue(new String(baos.toByteArray()).equals("testData"));
        byte[] rawData = ClusterClassLoaderUtils.verifyResourceData(signedData, null);
        assertTrue(new String(rawData).equals("testData"));
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(signserverhome + "/src/test/TESTCODESIGN.jks"), "foo123".toCharArray());
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(signserverhome + "/src/test/codesigntruststore.jks"), "foo123".toCharArray());
        signedData = ClusterClassLoaderUtils.generateCMSMessageFromResource(testData, (X509Certificate) ks.getCertificate("TESTCODESIGN"), (PrivateKey) ks.getKey("TESTCODESIGN", "foo123".toCharArray()), "BC");
        rawData = ClusterClassLoaderUtils.verifyResourceData(signedData, trustStore);
        assertTrue(new String(rawData).equals("testData"));
        testData = "testData".getBytes();
        signedData = ClusterClassLoaderUtils.generateCMSMessageFromResource(testData, null, null, "BC");
        try {
            ClusterClassLoaderUtils.verifyResourceData(signedData, ks);
            assertTrue(false);
        } catch (SignServerException e1) {
        }
        try {
            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(signserverhome + "/src/test/TESTNOCODESIGN.jks"), "foo123".toCharArray());
            signedData = ClusterClassLoaderUtils.generateCMSMessageFromResource(testData, (X509Certificate) ks.getCertificate("testnocodesign"), (PrivateKey) ks.getKey("testnocodesign", "foo123".toCharArray()), "BC");
            rawData = ClusterClassLoaderUtils.verifyResourceData(signedData, trustStore);
            assertTrue(false);
        } catch (SignatureException e) {
        }
        try {
            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(signserverhome + "/src/test/TESTCODESIGN.jks"), "foo123".toCharArray());
            trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(signserverhome + "/src/test/codesignnottruststore.jks"), "foo123".toCharArray());
            signedData = ClusterClassLoaderUtils.generateCMSMessageFromResource(testData, (X509Certificate) ks.getCertificate("TESTCODESIGN"), (PrivateKey) ks.getKey("TESTCODESIGN", "foo123".toCharArray()), "BC");
            rawData = ClusterClassLoaderUtils.verifyResourceData(signedData, trustStore);
            assertTrue(false);
        } catch (SignatureException e) {
        }
    }
