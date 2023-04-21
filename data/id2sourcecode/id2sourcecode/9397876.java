    private byte[] writeFile(File f, XDSDocumentWriter writer) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = null;
        DigestOutputStream dos = null;
        if (!f.exists()) {
            log.info("#### Write File:" + f);
            try {
                f.getParentFile().mkdirs();
                md = MessageDigest.getInstance("SHA1");
                FileOutputStream fos = new FileOutputStream(f);
                dos = new DigestOutputStream(fos, md);
                writer.writeTo(dos);
                log.info("#### File written:" + f + " exists:" + f.exists());
            } finally {
                if (dos != null) try {
                    dos.close();
                } catch (IOException ignore) {
                    log.error("Ignored error during close!", ignore);
                }
            }
        }
        return md == null ? null : md.digest();
    }
