    protected Object getRemoteContent() throws IOException {
        InputStream in = url.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileUtils.copyStream(in, out, true);
        return out.toString();
    }
