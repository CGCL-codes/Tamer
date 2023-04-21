    private String getFileHash(File f) {
        try {
            InputStream is = new FileInputStream(f);
            byte[] buf = new byte[64 * 1024];
            MessageDigest m = MessageDigest.getInstance("MD5");
            int iRead;
            do {
                iRead = is.read(buf);
                if (iRead > 0) m.update(buf, 0, iRead);
            } while (iRead != -1);
            is.close();
            BigInteger i = new BigInteger(1, m.digest());
            return (String.format("%1$032X", i));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
