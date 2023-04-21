    protected byte[] createHash(InputStream fis) {
        try {
            messageDigest.reset();
            int cnt = -1;
            while ((cnt = fis.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, cnt);
            }
            return messageDigest.digest();
        } catch (IOException e) {
            return null;
        }
    }
