    public int redirect(OutputStream out, int len) throws IOException {
        int read = read(buf);
        if (read < 0) {
            if (autoClose) out.close();
        } else {
            out.write(buf, 0, read);
            if (autoFlush) out.flush();
        }
        return read;
    }
