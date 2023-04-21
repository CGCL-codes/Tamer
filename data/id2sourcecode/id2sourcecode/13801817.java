    private void transfer(OutputStream os, String path) throws IOException {
        path = decodeUrlPath(path);
        File f = new File(path);
        FileInputStream file = new FileInputStream(f);
        long length = f.length();
        StringBuffer buf = new StringBuffer(OKHEADER).append(CONNECTION_CLOSE).append(SERVER).append(CONTENT_BINARY).append(CONTENT_LENGTH).append(" ").append(length).append(CRLF).append(CRLF);
        os.write(buf.toString().getBytes());
        os.flush();
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while (length != 0) {
            read = file.read(buffer);
            if (read == -1) break;
            os.write(buffer, 0, read);
            length -= read;
        }
        file.close();
        os.flush();
        os.close();
    }
