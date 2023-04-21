    public final void testReadbyteArrayintint04() throws IOException {
        for (int ii = 0; ii < algorithmName.length; ii++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithmName[ii]);
                InputStream is = new ByteArrayInputStream(myMessage);
                DigestInputStream dis = new DigestInputStream(is, md);
                byte[] bArray = new byte[MY_MESSAGE_LEN];
                dis.read(bArray, 0, bArray.length);
                assertEquals("retval1", -1, dis.read(bArray, 0, 1));
                assertEquals("retval2", -1, dis.read(bArray, 0, bArray.length));
                assertEquals("retval3", -1, dis.read(bArray, 0, 1));
                assertTrue("update", Arrays.equals(dis.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[ii])));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
