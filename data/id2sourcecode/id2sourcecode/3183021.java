    private void send(HTTPResponse response) throws IOException {
        logger.debug("Sending response: " + "type(" + response.getHttpResponseNumber() + ")" + " mime(" + response.getMimeType() + ")");
        if (response.getContent() instanceof InputStream) {
            InputStream input = (InputStream) response.getContent();
            byte[] buffer = new byte[10000];
            int count;
            do {
                count = input.read(buffer);
                if (count > 0) bufferedOutput.write(buffer, 0, count);
            } while (count > 0);
        } else if (response.getContent() instanceof File) {
            File file = (File) response.getContent();
            String mimeType = MimeType.getMimeType(file.getName());
            response.setMimeType(mimeType);
            InputStream input = new FileInputStream(file);
            response.setContent(StreamUtils.printStreamToByteArray(input));
            bufferedOutput.write(response.getResponseBytes());
        } else {
            bufferedOutput.write(response.getResponseBytes());
        }
        bufferedOutput.flush();
    }
