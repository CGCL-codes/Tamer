    private String genMessageDigest(final String packagePath) throws IOException {
        final String algorithm = "SHA-1";
        FileInputStream fin = new FileInputStream(packagePath);
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fin.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fin.close();
            return HostUtils.toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return algorithm + " not found";
        }
    }
