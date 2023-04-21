    public void receiveFile(Socket s, OutputStream out, Representation representation) throws IOException {
        InputStream in = representation.getInputStream(s);
        byte buf[] = new byte[BUFSIZ];
        int nread;
        while ((nread = in.read(buf, 0, BUFSIZ)) > 0) {
            out.write(buf, 0, nread);
        }
        in.close();
    }
