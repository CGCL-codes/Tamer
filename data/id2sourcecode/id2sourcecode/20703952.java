    public void testMessageDigest() {
        byte[] rndBytes = new byte[1024];
        rnd.nextBytes(rndBytes);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
        }
        byte[] digestBytes = md.digest(rndBytes);
    }
