    private static String getMD5Hash(String key) {
        byte[] bytes = md.digest(key.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append((int) (0x00FF & bytes[i]));
            if (i + 1 < bytes.length) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
