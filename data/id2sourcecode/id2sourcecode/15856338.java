    public BigInteger calculateMd5(FileObject file) throws FileSystemException {
        InputStream is = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192 * 1];
            int read = -1;
            is = file.getContent().getInputStream();
            while ((read = is.read(buffer)) >= 0) {
                digest.update(buffer, 0, read);
            }
            is.close();
            is = null;
            byte[] messageDigest = digest.digest();
            BigInteger bigInt = new BigInteger(1, messageDigest);
            return bigInt;
        } catch (Exception e) {
            throw new FileSystemException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
