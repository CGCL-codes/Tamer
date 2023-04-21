    public String createShadowKey(InputStream in) throws NoSuchAlgorithmException, IOException {
        String result;
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] buffer = new byte[1000000];
        int totalBytes = 0;
        try {
            int bytesRead;
            while ((bytesRead = in.read(buffer)) >= 0) {
                sha.update(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            result = new String(Hex.encodeHex(sha.digest())) + "-" + totalBytes;
        } finally {
            in.close();
        }
        return result;
    }
