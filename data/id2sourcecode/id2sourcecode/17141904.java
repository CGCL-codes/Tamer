    private void append(URL url) {
        try {
            InputStream is = url.openStream();
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                append(new String(buffer));
            }
        } catch (IOException IOE) {
            append(IOE.toString());
        }
    }
