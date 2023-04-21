    public final void testReadbyteArrayintint05() throws IOException {
        assertEquals(0, MY_MESSAGE_LEN % CHUNK_SIZE);
        for (int ii = 0; ii < algorithmName.length; ii++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithmName[ii]);
                InputStream is = new ByteArrayInputStream(myMessage);
                DigestInputStream dis = new DigestInputStream(is, md);
                byte[] bArray = new byte[MY_MESSAGE_LEN];
                dis.on(false);
                for (int i = 0; i < MY_MESSAGE_LEN / CHUNK_SIZE; i++) {
                    dis.read(bArray, i * CHUNK_SIZE, CHUNK_SIZE);
                }
                assertTrue(Arrays.equals(dis.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[ii] + "_NU")));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
