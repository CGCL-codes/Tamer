    public void putFile(User user, Object securitySetup, File file, URI uri, CommonTargetSystemResource tsr) throws Exception {
        URL url = new URL(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath());
        URLConnection connection = url.openConnection();
        connection.setDoInput(false);
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        int chunk = 16384;
        long ofs = 0;
        while (true) {
            byte[] buf = tsr.getChunk(user, file, ofs, chunk);
            if (buf != null && buf.length > 0) {
                os.write(buf, 0, buf.length);
                ofs += buf.length;
            } else {
                break;
            }
        }
        os.close();
    }
