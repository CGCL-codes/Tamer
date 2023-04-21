    public static byte[] getMessageDigest(InputStream aInput) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] data = new byte[512];
            for (int bytesRead, i = 0; (bytesRead = aInput.read(data)) > 0; i += bytesRead) md.update(data, 0, bytesRead);
            return md.digest();
        } catch (Exception e) {
        }
        return null;
    }
