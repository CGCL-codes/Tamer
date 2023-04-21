    public String encrypt(File file) throws IOException {
        byte[] hash = null;
        try {
            FileInputStream in = new FileInputStream(file);
            MessageDigest instance = MessageDigest.getInstance(getAlgorithm());
            byte array[] = new byte[2048];
            int len;
            while ((len = in.read(array)) != -1) {
                instance.update(array, 0, len);
            }
            hash = instance.digest();
            in.close();
        } catch (NoSuchAlgorithmException err) {
            throw new IOException(err);
        } catch (UnsupportedEncodingException err) {
            throw new IOException(err);
        }
        return getHashString(hash);
    }
