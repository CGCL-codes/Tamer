    public byte[] resolveRef(String url) throws MalformedURLException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        URL addressurl = new URL(url);
        InputStream in = addressurl.openStream();
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = in.read(buffer, 0, bufferSize)) != -1) {
            out.write(buffer, 0, bytesRead);
            out.flush();
        }
        byte[] bout = out.toByteArray();
        in.close();
        out.close();
        return bout;
    }
