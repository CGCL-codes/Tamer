    private byte[] download(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        uc.setConnectTimeout(10000);
        DataInputStream di = new DataInputStream(uc.getInputStream());
        byte[] buffer = new byte[uc.getContentLength()];
        di.readFully(buffer);
        di.close();
        return buffer;
    }
