    public SHA1Sum(File f) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        InputStream is = new FileInputStream(f);
        byte[] buffer = new byte[8192];
        do {
            int read = is.read(buffer);
            if (read <= 0) break;
            digest.update(buffer, 0, read);
        } while (true);
        sha1sum = digest.digest();
        if (Out.isDebug(SHA1Sum.class)) Out.debugAlways(getClass(), f.getName() + ": " + toString());
    }
