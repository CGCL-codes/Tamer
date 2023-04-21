    protected static String SHA1it(String message) {
        MessageDigest encryptor;
        StringBuffer encrypted;
        byte[] bmsg = message.getBytes();
        boolean more2 = true;
        try {
            encryptor = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("This JVM stinks. Get one that implements SHA.");
            System.err.println("Error: " + nsae);
            return null;
        }
        for (int i = 0; i < bmsg.length; i++) encryptor.update(bmsg[i]);
        bmsg = encryptor.digest();
        encrypted = new StringBuffer();
        for (int i = 0; i < bmsg.length; i++) {
            int t = bmsg[i] & 0xFF;
            if (t <= 0x0F) encrypted.append("0");
            encrypted.append(Integer.toHexString(t));
        }
        return encrypted.toString();
    }
