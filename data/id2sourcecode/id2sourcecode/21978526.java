    public static String sum(InputStream stream) throws Exception {
        byte[] buffer = new byte[1024];
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = stream.read(buffer);
            if (numRead > 0) {
                messageDigest.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        stream.close();
        return toHex(messageDigest.digest());
    }
