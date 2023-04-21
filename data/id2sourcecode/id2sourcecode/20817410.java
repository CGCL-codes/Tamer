    public static String generateMD5(InputStream md5Stream) throws NoSuchAlgorithmException, IOException {
        String md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            byte[] bytes = new byte[1024];
            int count = 0;
            while ((count = md5Stream.read(bytes)) != -1) {
                md.update(bytes, 0, count);
            }
            byte[] md5Digest = md.digest();
            StringBuffer buffer = new StringBuffer();
            for (byte b : md5Digest) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(hex);
            }
            md5 = buffer.toString();
        } catch (NoSuchAlgorithmException ex) {
            log.error("MD5 Algorithm Not found");
            throw ex;
        } finally {
            if (md5Stream != null) {
                md5Stream.close();
            }
        }
        return md5;
    }
