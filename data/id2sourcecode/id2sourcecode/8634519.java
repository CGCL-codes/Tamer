    protected void init(HttpURLConnection connection, boolean pull, OutputStream out) throws IOException {
        if (connection.getResponseCode() < 400) {
            Map metadata = extractMetadata(connection);
            in = connection.getInputStream();
            if (pull) if (out == null) {
                byte[] body = slurpInputStream(in);
                this.object = new S3Object(body, metadata);
            } else {
                byte[] buf = new byte[READ_BUF_SIZE];
                BufferedInputStream bin = new BufferedInputStream(in, READ_BUF_SIZE);
                BufferedOutputStream bout = new BufferedOutputStream(out, READ_BUF_SIZE);
                int count;
                while ((count = bin.read(buf)) != -1) bout.write(buf, 0, count);
                bout.flush();
            }
        }
    }
