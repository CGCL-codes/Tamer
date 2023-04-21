    public static String md5(String data) {
        String result = "";
        if (data == null || data.isEmpty()) return null;
        int lsb = 0;
        digest.update(data.getBytes());
        byte b[] = digest.digest();
        for (int i = 0; i < b.length; i++) {
            int msb = (b[i] & 0xff) / 16;
            lsb = (b[i] & 0xff) % 16;
            result = (new StringBuilder()).append(result).append(hexChars[msb]).append(hexChars[lsb]).toString();
        }
        return result.toString();
    }
