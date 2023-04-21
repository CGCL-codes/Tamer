    protected static String getMapHash(String mapFile) {
        InputStream i = new Object().getClass().getResourceAsStream("/org/saiko/ai/genetics/tsp/etc/" + mapFile + ".csv");
        if (i == null) return null;
        try {
            byte b[] = new byte[1024];
            int size;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            while ((size = i.read(b)) > 0) {
                buffer.write(b, 0, size);
            }
            i.close();
            MessageDigest digest = MessageDigest.getInstance("SHA");
            return new BASE64Encoder().encode(digest.digest(buffer.toByteArray()));
        } catch (Throwable e) {
            return null;
        }
    }
