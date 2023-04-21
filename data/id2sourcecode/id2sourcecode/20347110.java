    @Override
    protected byte[] sendRequest(final TimeStampRequest request) throws IOException {
        URLConnection connection = null;
        try {
            connection = this.url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            this.setConnectionProperties(connection);
            byte[] requestBytes = request.getEncoded();
            OutputStream outputStream = connection.getOutputStream();
            this.writeBytes(outputStream, requestBytes);
            outputStream.close();
            InputStream inputStream = connection.getInputStream();
            String encoding = connection.getContentEncoding();
            byte[] bytes = this.readBytes(inputStream, encoding);
            return bytes;
        } catch (IOException e) {
            throw e;
        }
    }
