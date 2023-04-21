    protected String createDataUrl(String src) throws IOException {
        String result = null;
        InputStream inputStream = isImageResource(src) ? getResource(src) : null;
        if (inputStream != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
            byte[] bytes = new byte[512];
            int readBytes;
            while ((readBytes = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, readBytes);
            }
            String format = "png";
            int dot = src.lastIndexOf('.');
            if (dot > 0 && dot < src.length()) {
                format = src.substring(dot + 1);
            }
            result = "data:image/" + format + ";base64," + mxBase64.encodeToString(outputStream.toByteArray(), false);
        }
        return result;
    }
