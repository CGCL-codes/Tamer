    public static String getDigestHash(String str, String code, String hashType) {
        if (str == null) return null;
        try {
            byte codeBytes[] = null;
            if (code == null) codeBytes = str.getBytes(); else codeBytes = str.getBytes(code);
            MessageDigest messagedigest = MessageDigest.getInstance(hashType);
            messagedigest.update(codeBytes);
            byte digestBytes[] = messagedigest.digest();
            int i = 0;
            char digestChars[] = new char[digestBytes.length * 2];
            for (int j = 0; j < digestBytes.length; j++) {
                int k = digestBytes[j];
                if (k < 0) {
                    k = 127 + k * -1;
                }
                StringUtil.encodeInt(k, i, digestChars);
                i += 2;
            }
            return new String(digestChars, 0, digestChars.length);
        } catch (Exception e) {
            Debug.logError(e, "Error while computing hash of type " + hashType, module);
        }
        return str;
    }
