    public static String checksum(File file, MessageDigest md) throws InterruptedException {
        try {
            md.reset();
            FileInputStream in = new FileInputStream(file);
            byte buf[] = BufferPool.getInstance().get(1024);
            int len;
            try {
                while ((len = in.read(buf)) != -1) {
                    md.update(buf, 0, len);
                    Utils.checkCancel();
                }
                byte digestBytes[] = md.digest();
                return Utils.formatDigest(digestBytes);
            } finally {
                BufferPool.getInstance().put(buf);
                in.close();
            }
        } catch (IOException ex) {
            Utils.log(LOG_ERROR, "Error in checksum", ex);
        }
        return null;
    }
