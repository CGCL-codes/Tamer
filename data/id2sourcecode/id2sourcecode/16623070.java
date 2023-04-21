    @Override
    public String computeMd5Hash(String path) {
        if (logger.isDebugEnabled()) {
            logger.debug("Computing md5 for file: " + path);
        }
        File file = new File(path);
        String result = null;
        InputStream fin = null;
        try {
            fin = new FileInputStream(file);
            MessageDigest md5er = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            do {
                read = fin.read(buffer);
                if (read > 0) {
                    md5er.update(buffer, 0, read);
                }
            } while (read != -1);
            byte[] digest = md5er.digest();
            if (digest != null) {
                StringBuffer strDigest = new StringBuffer();
                for (int i = 0; i < digest.length; i++) {
                    strDigest.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
                }
                result = strDigest.toString();
            }
        } catch (Exception e) {
            logger.error("Error while computing md5 on file: " + path, e);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException ioe) {
                    logger.error("Error while closing stream on file: " + path, ioe);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("md5 hash is: " + result);
        }
        return result;
    }
