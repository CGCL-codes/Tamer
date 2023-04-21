    private void unwrapFromInputStream(boolean forHandshake) throws IOException {
        SSLEngineResult result;
        ByteBuffer cryptBuffer;
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        boolean keepRun = true;
        boolean closed = false;
        int oldPosition = clearInputBuffer.position();
        clearInputBuffer.position(clearInputBuffer.limit());
        clearInputBuffer.limit(clearInputBuffer.capacity());
        byteOutputStream.write(inputStream.read());
        while (keepRun) {
            cryptBuffer = ByteBuffer.wrap(byteOutputStream.toByteArray());
            result = sslEngine.unwrap(cryptBuffer, clearInputBuffer);
            switch(result.getStatus()) {
                case BUFFER_OVERFLOW:
                    enlargeClearInputBuffer();
                    oldPosition = clearInputBuffer.position();
                    clearInputBuffer.position(clearInputBuffer.limit());
                    clearInputBuffer.limit(clearInputBuffer.capacity());
                    break;
                case BUFFER_UNDERFLOW:
                    byteOutputStream.write(inputStream.read());
                    break;
                case CLOSED:
                    keepRun = false;
                    closed = true;
                    break;
                case OK:
                    clearInputBuffer.limit(clearInputBuffer.position());
                    clearInputBuffer.position(oldPosition);
                    if (forHandshake) {
                        keepRun = false;
                    } else {
                        if (cryptBuffer.position() >= cryptBuffer.limit()) {
                            keepRun = false;
                        } else {
                            cryptBuffer = cryptBuffer.slice();
                            byteOutputStream = new ByteArrayOutputStream();
                            byteOutputStream.write(cryptBuffer.array());
                        }
                    }
                    break;
            }
        }
        if (closed) {
            throw new IOException("Other party closed the SSL connection");
        }
    }
