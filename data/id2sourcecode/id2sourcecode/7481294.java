    public void testGetDigestOutputStreams() throws Exception {
        File testFile = new File("test/FileCheckSummer");
        File copyTestFile = new File("test/FileCheckSummer2");
        testFile.delete();
        copyTestFile.delete();
        assertTrue(!testFile.exists());
        assertTrue(!copyTestFile.exists());
        for (long length = 1; length <= 512; length++) {
            FileOutputStream out = new FileOutputStream(testFile);
            DigestOutputStream dOut = CheckSummer.getDigestOutputStream(out);
            fill(dOut, length);
            byte[] digesfromStream = dOut.getMessageDigest().digest();
            byte[] digestExpected = CheckSummer.createChecksum(testFile.getAbsolutePath());
            assertEquals(digestExpected, digesfromStream);
            dOut.close();
            out.close();
            testFile.delete();
        }
    }
