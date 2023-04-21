    public String calculate(Object o) {
        StringBuffer buffer = new StringBuffer();
        MessageDigest messageDigest;
        byte[] digest;
        File file;
        try {
            file = (File) o;
        } catch (ClassCastException e) {
            System.err.println("Unable to cast the object to a File: " + o);
            return null;
        }
        if (!file.exists()) {
            System.err.println("Unable to calculate ETag; file doesn't exist: " + file);
            return null;
        }
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        if ((flags & FLAG_CONTENT) != 0) {
            try {
                DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(file), messageDigest);
                byte[] b = new byte[1024];
                while (digestInputStream.read(b, 0, b.length) > 0) {
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        if ((flags & FLAG_MTIME) != 0) {
            buffer.append(file.lastModified());
        }
        if ((flags & FLAG_SIZE) != 0) {
            buffer.append(file.length());
        }
        digest = messageDigest.digest(buffer.toString().getBytes());
        return new String(Hex.encodeHex(digest));
    }
