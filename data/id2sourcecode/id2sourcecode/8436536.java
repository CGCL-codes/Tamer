    public final void testRead03() throws IOException {
        for (int ii = 0; ii < algorithmName.length; ii++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithmName[ii]);
                InputStream is = new ByteArrayInputStream(myMessage);
                DigestInputStream dis = new DigestInputStream(is, md);
                dis.on(false);
                for (int i = 0; i < MY_MESSAGE_LEN; i++) {
                    dis.read();
                }
                assertTrue(Arrays.equals(dis.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[ii] + "_NU")));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
