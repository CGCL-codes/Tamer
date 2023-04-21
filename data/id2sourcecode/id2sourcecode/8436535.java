    public final void testRead02() throws IOException {
        for (int ii = 0; ii < algorithmName.length; ii++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithmName[ii]);
                InputStream is = new ByteArrayInputStream(myMessage);
                DigestInputStream dis = new DigestInputStream(is, md);
                for (int i = 0; i < MY_MESSAGE_LEN; i++) {
                    dis.read();
                }
                assertEquals("retval1", -1, dis.read());
                assertEquals("retval2", -1, dis.read());
                assertEquals("retval3", -1, dis.read());
                assertTrue("update", Arrays.equals(dis.getMessageDigest().digest(), MDGoldenData.getDigest(algorithmName[ii])));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
