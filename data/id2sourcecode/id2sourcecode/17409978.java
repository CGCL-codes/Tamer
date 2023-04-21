    protected boolean writeRequestBody(HttpState state, HttpConnection conn) throws IOException, HttpException {
        OutputStream out = conn.getRequestOutputStream();
        if (isHttp11() && (null == getRequestHeader("Content-Length"))) {
            out = new ChunkedOutputStream(out);
        }
        InputStream inputStream = null;
        if (file != null && file.exists()) {
            inputStream = new FileInputStream(file);
        } else if (url != null) {
            inputStream = url.openConnection().getInputStream();
        } else if (data != null) {
            inputStream = new ByteArrayInputStream(data);
        } else {
            return true;
        }
        byte[] buffer = new byte[4096];
        int nb = 0;
        while (true) {
            nb = inputStream.read(buffer);
            if (nb == -1) {
                break;
            }
            out.write(buffer, 0, nb);
        }
        out.flush();
        return true;
    }
