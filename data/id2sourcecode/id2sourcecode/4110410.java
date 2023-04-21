    public void put(String uri, InputStream is, Header[] headers) throws IOException {
        File f = tf.getFile();
        FileOutputStream os = new FileOutputStream(f);
        byte[] buf = new byte[1024];
        for (int nRead; (nRead = is.read(buf, 0, 1024)) > 0; ) os.write(buf, 0, nRead);
        uriHt.put(uri, f.getAbsolutePath());
        headersHt.put(uri, headers);
    }
