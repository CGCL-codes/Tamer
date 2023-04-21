    private void writeBody(DataOutput out) throws IOException {
        out.writeBytes(CRLF);
        if (content == null) {
            out.write(body);
        } else {
            InputStream in = content.getInputStream();
            byte buf[] = new byte[1024];
            for (int i = 0; (i = in.read(buf)) != -1; out.write(buf, 0, i)) ;
        }
    }
