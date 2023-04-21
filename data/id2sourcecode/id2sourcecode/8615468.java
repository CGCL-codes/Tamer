    public void seek(long pos) throws IOException {
        if (pos >= fp) {
            skipBytes((int) (pos - fp));
            return;
        } else if (pos >= mark) {
            try {
                is.reset();
                fp = mark;
                skipBytes((int) (pos - fp));
                return;
            } catch (IOException e) {
            }
        }
        close();
        conn = (HttpURLConnection) (new URL(url)).openConnection();
        conn.setDoOutput(true);
        if (is != null) {
            is = new DataInputStream(new BufferedInputStream(conn.getInputStream(), 65536));
            is.mark((int) length());
            mark = 0;
        }
        if (os != null) os = new DataOutputStream(conn.getOutputStream());
        this.url = url;
        fp = 0;
        skipBytes((int) pos);
    }
