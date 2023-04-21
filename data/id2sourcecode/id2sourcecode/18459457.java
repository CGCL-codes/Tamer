    public void setCommandContext(String verb, DataHandler dh) throws IOException {
        this.dh = dh;
        InputStream in = dh.getInputStream();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int len = in.read(buf); len != -1; len = in.read(buf)) bytes.write(buf, 0, len);
        in.close();
        setText(bytes.toString());
    }
