    public void streamOutput(InputStream in, ResponseInterface responseInterface) throws IOException {
        OutputStream out = responseInterface.openStream();
        byte[] buffer = new byte[TRANSFER_BUF_SIZE];
        int readLength;
        while ((readLength = in.read(buffer)) > 0) {
            out.write(buffer, 0, readLength);
        }
        in.close();
        out.close();
    }
