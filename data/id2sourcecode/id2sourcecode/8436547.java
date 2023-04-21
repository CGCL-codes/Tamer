    public final void testOn() throws IOException {
        for (int ii = 0; ii < algorithmName.length; ii++) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithmName[ii]);
                InputStream is = new ByteArrayInputStream(myMessage);
                DigestInputStream dis = new DigestInputStream(is, md);
                dis.on(false);
                for (int i = 0; i < MY_MESSAGE_LEN - 1; i++) {
                    dis.read();
                }
                dis.on(true);
                dis.read();
                byte[] digest = dis.getMessageDigest().digest();
                assertFalse(Arrays.equals(digest, MDGoldenData.getDigest(algorithmName[ii])) || Arrays.equals(digest, MDGoldenData.getDigest(algorithmName[ii] + "_NU")));
                return;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        fail(getName() + ": no MessageDigest algorithms available - test not performed");
    }
