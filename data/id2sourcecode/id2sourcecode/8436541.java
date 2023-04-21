    public final void testReadbyteArrayintint02() throws IOException {
        assertEquals(0, MY_MESSAGE_LEN % CHUNK_SIZE);
        for (int ii = 0; ii < algorithmName.length; ii++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithmName[ii]);
                InputStream is = new ByteArrayInputStream(myMessage);
                DigestInputStream dis = new DigestInputStream(is, md);
                byte[] bArray = new byte[MY_MESSAGE_LEN];
                for (int i = 0; i < MY_MESSAGE_LEN / CHUNK_SIZE; i++) {
                    assertTrue("retval", dis.read(bArray, i * CHUNK_SIZE, CHUNK_SIZE) == CHUNK_SIZE);
                }
                assertTrue("bArray", Arrays.equals(myMessage, bArray));
                assertTrue("update", Arrays.equals(dis.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[ii])));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
