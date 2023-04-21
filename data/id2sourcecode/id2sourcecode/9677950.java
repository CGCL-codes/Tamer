    private static byte[] computeHash(File f) throws IOException {
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("This sould not have happend -- a very strange error must have occured.");
        }
        byte[] md = new byte[8192];
        InputStream in = new FileInputStream(f);
        int n = 0;
        while ((n = in.read(md)) > -1) {
            messagedigest.update(md, 0, n);
        }
        return messagedigest.digest();
    }
