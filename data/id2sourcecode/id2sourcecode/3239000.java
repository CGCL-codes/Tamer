    public static String createHashCode(String file) {
        try {
            File f = new File(file);
            if (f.exists()) {
                InputStream is = null;
                is = new FileInputStream(f);
                byte[] bytes = new byte[(int) 256];
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }
                is.close();
                MessageDigest m = MessageDigest.getInstance("MD5");
                m.update(bytes, 0, 256);
                return new BigInteger(1, m.digest()).toString();
            }
            return null;
        } catch (Exception exc) {
            log.error("Could not create MD5 has for " + file, exc);
            return null;
        }
    }
