    protected boolean writeRequestBody(HttpState state, HttpConnection conn) throws IOException, HttpException {
        OutputStream out = conn.getRequestOutputStream();
        if (isHttp11() && getRequestContentLength() == -1) {
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
        if (out instanceof ChunkedOutputStream) {
            try {
                if (chunkedOutputStreamFinish == null) {
                    chunkedOutputStreamFinish = out.getClass().getMethod("finish", null);
                }
                chunkedOutputStreamFinish.invoke(out, null);
            } catch (NoSuchMethodException nsme) {
            } catch (Exception e) {
                throw new HttpException(e.getClass() + ": " + e.getMessage());
            }
        }
        out.flush();
        return true;
    }
