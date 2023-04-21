    private static String calcChecksumNoWhiteSpace(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(SECRET);
            char[] chars = str.toCharArray();
            StringBuilder trimmed = new StringBuilder();
            for (char c : chars) if (c != ' ' && c != '\r' && c != '\n' && c != '\t') trimmed.append(c);
            byte[] md5 = md.digest(trimmed.toString().getBytes());
            long ret = ((long) md5[0]) + (((long) md5[1]) << 8) + (((long) md5[2]) << 16) + (((long) md5[3]) << 24) + (((long) md5[4]) << 32) + (((long) md5[5]) << 40) + (((long) md5[6]) << 48) + (((long) md5[7]) << 56);
            return Long.toHexString(ret);
        } catch (NoSuchAlgorithmException e) {
            return "0";
        }
    }
