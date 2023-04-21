    public static String checksum(File file, String method) {
        if (!file.exists()) return null;
        try {
            InputStream fin = new FileInputStream(file);
            java.security.MessageDigest md5er = MessageDigest.getInstance(method);
            byte[] buffer = new byte[1024];
            int read;
            do {
                read = fin.read(buffer);
                if (read > 0) md5er.update(buffer, 0, read);
            } while (read != -1);
            fin.close();
            byte[] digest = md5er.digest();
            if (digest == null) return null;
            String strDigest = "0x";
            for (int i = 0; i < digest.length; i++) {
                strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
            }
            return strDigest;
        } catch (Exception e) {
            return null;
        }
    }
