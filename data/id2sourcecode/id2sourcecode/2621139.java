    public static String encode(String message) {
        String hashString = null;
        messageDigest.reset();
        messageDigest.update(message.getBytes());
        byte hashCode[] = messageDigest.digest();
        hashString = "";
        for (int i = 0; i < hashCode.length; i++) {
            int x = hashCode[i] & 0xff;
            if (x < 16) {
                hashString += '0';
            }
            hashString += Integer.toString(x, 16);
        }
        return hashString;
    }
