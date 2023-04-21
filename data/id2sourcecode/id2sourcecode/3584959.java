    String hashStream(InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream, 4096);
        byte[] buffer = new byte[4096];
        int count = 0;
        int n;
        while ((n = bis.read(buffer)) != -1) if (n > 0) {
            sha.update(buffer, 0, n);
            count += n;
        }
        byte[] hash = sha.digest();
        if (Configuration.DEBUG) log.finest("Hashed " + count + " byte(s)");
        String result = Base64.encode(hash);
        return result;
    }
