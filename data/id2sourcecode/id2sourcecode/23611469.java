    public static String read(String url, int maxLen) {
        maxLen = maxLen <= 0 ? 1024 : maxLen > 4096 ? 4096 : maxLen;
        StringBuilder sb = new StringBuilder(maxLen);
        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            int num = 0;
            byte[] buf = new byte[maxLen];
            InputStream ins = new BufferedInputStream(conn.getInputStream(), maxLen);
            while ((num = ins.read(buf)) != -1) {
                int rem = Math.min(maxLen - sb.length(), num);
                sb.append(new String(buf, 0, rem));
                if (sb.length() == maxLen) {
                    break;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("Fail to read url " + url + " - " + e);
            return null;
        }
    }
