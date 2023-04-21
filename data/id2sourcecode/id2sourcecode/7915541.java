    private byte[] digestFile(final String file, final String digestAlg) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
        final MessageDigest dig = MessageDigest.getInstance(digestAlg, "BC");
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            final byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                dig.update(buf, 0, len);
            }
            return dig.digest();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }
    }
