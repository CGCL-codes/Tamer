    public Response request(Uri uri) throws Exception {
        Response resp = new Response();
        resp.session = this;
        InputStream in = open(uri);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int c;
        while ((c = in.read()) >= 0) bout.write(c);
        in.close();
        byte[] buf = bout.toByteArray();
        StringBuffer source = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; ++i) {
            c = buf[i];
            if (c == '\n' || (' ' <= c && c <= 0x7F)) source.append((char) c); else source.append('.');
        }
        resp.source = source.toString();
        try {
            resp.obj = new ObixDecoder(new ByteArrayInputStream(buf)).decodeDocument();
        } catch (Throwable e) {
            resp.objError = e;
        }
        return resp;
    }
