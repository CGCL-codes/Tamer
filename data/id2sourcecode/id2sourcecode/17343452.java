    private void readMessageBody(InputStream inputStream) throws IOException {
        if ("chunked".equalsIgnoreCase(getHeader("Transfer-Encoding"))) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (getNextChunkLength(inputStream) > 0) {
                baos.write(readDelimitedChunk(inputStream));
            }
            flushChunkTrailer(inputStream);
            _requestBody = baos.toByteArray();
        } else {
            int totalExpected = getContentLength();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(totalExpected);
            byte[] buffer = new byte[1024];
            int total = 0;
            int count = -1;
            while ((total < totalExpected) && ((count = inputStream.read(buffer)) != -1)) {
                baos.write(buffer, 0, count);
                total += count;
            }
            baos.flush();
            _requestBody = baos.toByteArray();
        }
        _reader = new InputStreamReader(new ByteArrayInputStream(_requestBody));
    }
