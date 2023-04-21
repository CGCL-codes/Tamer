    public void fetchFile(User user, Object securitySetup, URI uri, File file, CommonTargetSystemResource tsr) throws Exception {
        URL url = new URL(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath());
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        InputStream is = connection.getInputStream();
        long read = 0;
        int s = 0;
        int chunk = 16384;
        byte[] buf = new byte[chunk];
        while (s != -1) {
            s = is.read(buf, 0, chunk);
            tsr.putChunk(user, file, read == 0, buf, read, s);
            read += s;
        }
        is.close();
        tsr.changeOwner(user, file, user);
    }
