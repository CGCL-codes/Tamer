    public static String getDigestHash(String str, String hashType) {
        if (str == null) return null;
        try {
            MessageDigest messagedigest = MessageDigest.getInstance(hashType);
            byte strBytes[] = str.getBytes();
            messagedigest.update(strBytes);
            byte digestBytes[] = messagedigest.digest();
            int k = 0;
            char digestChars[] = new char[digestBytes.length * 2];
            for (int l = 0; l < digestBytes.length; l++) {
                int i1 = digestBytes[l];
                if (i1 < 0) i1 = 127 + i1 * -1;
                StringUtil.encodeInt(i1, k, digestChars);
                k += 2;
            }
            return new String(digestChars, 0, digestChars.length);
        } catch (Exception e) {
            Debug.logError(e, "Error while computing hash of type " + hashType, module);
        }
        return str;
    }
