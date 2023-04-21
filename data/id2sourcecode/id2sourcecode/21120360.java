    public static byte[] createDocumentId() {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        long time = System.currentTimeMillis();
        long mem = Runtime.getRuntime().freeMemory();
        String s = time + "+" + mem + "+" + (seq++);
        return md5.digest(s.getBytes());
    }
