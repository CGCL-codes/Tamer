    public static String hexsafe(String s) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (isSafe(ch)) {
                result.append(ch);
            } else {
                byte[] utf8 = null;
                try {
                    utf8 = s.substring(i, i + 1).getBytes("UTF-8");
                    for (int j = 0; j < utf8.length; j++) {
                        result.append('_');
                        result.append(hexdigits[(utf8[j] >> 4) & 0xF]);
                        result.append(hexdigits[utf8[j] & 0xF]);
                    }
                } catch (UnsupportedEncodingException uee) {
                    result.append("_BAD_UTF8_CHAR");
                }
            }
        }
        if (result.length() <= MAX_NAME_LENGTH) return result.toString();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] inputBytes = null;
            try {
                inputBytes = s.getBytes("UTF-8");
            } catch (UnsupportedEncodingException uee) {
                inputBytes = new byte[0];
            }
            byte[] digest = md.digest(inputBytes);
            assert (digest.length == 20);
            result = new StringBuffer(URI_SHA1_PREFIX);
            for (int j = 0; j < digest.length; j++) {
                result.append(hexdigits[(digest[j] >> 4) & 0xF]);
                result.append(hexdigits[digest[j] & 0xF]);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Using in a JDK without an SHA implementation");
        }
    }
