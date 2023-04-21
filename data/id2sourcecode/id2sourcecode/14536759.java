    public static byte[] md5String(String message) {
        byte[] input = message.getBytes();
        MessageDigest hash;
        ByteArrayInputStream bIn = null;
        DigestInputStream dIn = null;
        try {
            hash = MessageDigest.getInstance("MD5");
            bIn = new ByteArrayInputStream(input);
            dIn = new DigestInputStream(bIn, hash);
            for (int i = 0; i < input.length; i++) {
                dIn.read();
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "md5String(): " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "md5String(): " + e.toString());
        }
        return dIn.getMessageDigest().digest();
    }
