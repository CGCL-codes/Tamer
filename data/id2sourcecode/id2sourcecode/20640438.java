    private void computeDigest(File f, long offset, long len) throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(Constants.hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
        }
        FileInputStream fis = new FileInputStream(f);
        fis.skip(offset);
        byte[] buffer = new byte[2000];
        long count = 0;
        int l;
        while ((l = fis.read(buffer, 0, (int) (buffer.length < len - count ? buffer.length : len - count))) > 0 && count < len) {
            md.update(buffer, 0, l);
            count += l;
        }
        digest = md.digest();
    }
