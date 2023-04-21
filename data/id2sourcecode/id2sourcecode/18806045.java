    public InputStreamResource(String name, InputStream in) throws IOException {
        _name = name;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte buffer[] = new byte[2048];
        int bytes_read = 0;
        while ((bytes_read = in.read(buffer)) > 0) out.write(buffer, 0, bytes_read);
        out.flush();
        out.close();
        in.close();
        _content = out.toByteArray();
        _contentLength = _content.length;
        T.debug("contentLength=" + _contentLength);
        _contentType = MimeUtils.guessContentTypeFromName(name);
    }
