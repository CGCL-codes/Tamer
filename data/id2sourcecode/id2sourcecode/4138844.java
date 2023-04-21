    public void connect() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        if (closeIn) {
            in.close();
        }
        if (closeOut) {
            out.close();
        }
    }
