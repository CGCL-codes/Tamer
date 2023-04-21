    public static String getDigestAsHex(String value) {
        byte[] digested = digest.digest(value.getBytes());
        char[] hexStringChars = new char[digested.length * 2];
        int j = 0;
        for (int i = 0; i < digested.length; i++) {
            hexStringChars[j] = hexDigits[(digested[i] >>> 4) & 0xf];
            hexStringChars[j + 1] = hexDigits[digested[i] & 0xf];
            j += 2;
        }
        return new String(hexStringChars);
    }
