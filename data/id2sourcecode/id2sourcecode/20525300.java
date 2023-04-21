    public int[] getKeyHash(int clientToken, int serverToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] warBuf = new byte[26];
            IntFromByteArray.LITTLEENDIAN.insertInteger(warBuf, 0, clientToken);
            IntFromByteArray.LITTLEENDIAN.insertInteger(warBuf, 4, serverToken);
            IntFromByteArray.LITTLEENDIAN.insertInteger(warBuf, 8, getProduct());
            IntFromByteArray.LITTLEENDIAN.insertInteger(warBuf, 12, getVal1());
            for (int i = 16; i < 26; i++) warBuf[i] = getWar3Val2()[i - 16];
            digest.update(warBuf);
            return IntFromByteArray.LITTLEENDIAN.getIntArray(digest.digest());
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("Could not find SHA1 library " + e);
            System.exit(1);
            return new int[1];
        }
    }
