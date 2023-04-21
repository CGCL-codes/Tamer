    public void sendFile(InputStream in, Socket s, Representation representation) throws IOException {
        OutputStream out = representation.getOutputStream(s);
        byte buf[] = new byte[BUFSIZ];
        int nread;
        while ((nread = in.read(buf)) > 0) {
            out.write(buf, 0, nread);
        }
        out.close();
    }
