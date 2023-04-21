    public byte[] readSource(String source) throws IOException {
        byte[] result = null;
        URL urlsource = new URL(source);
        URLConnection consource = urlsource.openConnection();
        InputStream is = consource.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffor = new byte[102400];
        int length = -1;
        while ((length = is.read(buffor)) > 0) {
            baos.write(buffor, 0, length);
        }
        result = baos.toByteArray();
        baos.close();
        is.close();
        return result;
    }
