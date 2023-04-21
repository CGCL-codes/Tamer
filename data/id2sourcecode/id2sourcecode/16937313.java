    @Override
    protected void run(InputStream inputStream, OutputStream outputStream) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[8192];
        int numRead;
        while (!cancelled() && (numRead = inputStream.read(buffer)) != -1) {
            md.update(buffer, 0, numRead);
        }
        if (cancelled()) {
            return;
        }
        String checksum = getHex(md.digest());
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        writer.write(checksum);
        writer.flush();
    }
