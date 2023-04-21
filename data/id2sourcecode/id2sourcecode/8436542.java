    public final void testReadbyteArrayintint03() throws IOException {
        assertTrue(MY_MESSAGE_LEN % (CHUNK_SIZE + 1) != 0);
        for (int ii = 0; ii < algorithmName.length; ii++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithmName[ii]);
                InputStream is = new ByteArrayInputStream(myMessage);
                DigestInputStream dis = new DigestInputStream(is, md);
                byte[] bArray = new byte[MY_MESSAGE_LEN];
                for (int i = 0; i < MY_MESSAGE_LEN / (CHUNK_SIZE + 1); i++) {
                    assertTrue("retval1", dis.read(bArray, i * (CHUNK_SIZE + 1), CHUNK_SIZE + 1) == CHUNK_SIZE + 1);
                }
                assertTrue("retval2", dis.read(bArray, MY_MESSAGE_LEN / (CHUNK_SIZE + 1) * (CHUNK_SIZE + 1), MY_MESSAGE_LEN % (CHUNK_SIZE + 1)) == (MY_MESSAGE_LEN % (CHUNK_SIZE + 1)));
                assertTrue("bArray", Arrays.equals(myMessage, bArray));
                assertTrue("update", Arrays.equals(dis.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[ii])));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
