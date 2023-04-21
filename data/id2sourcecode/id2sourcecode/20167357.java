    private void chunkData(WebConnection wc, InputStream is) throws IOException {
        byte[] buf = new byte[1024];
        int read;
        HTTPOutputStream os = wc.getOutputStream();
        while ((read = is.read(buf)) > 0) os.write(buf, 0, read);
        os.finish();
    }
