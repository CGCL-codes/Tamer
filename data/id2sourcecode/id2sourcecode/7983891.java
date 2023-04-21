    protected HttpRequest copy(final InputStream input, final OutputStream output) throws RequestException {
        final byte[] buffer = new byte[bufferSize];
        int read;
        try {
            while ((read = input.read(buffer)) != -1) output.write(buffer, 0, read);
        } catch (IOException e) {
            throw new RequestException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                throw new RequestException(e);
            }
        }
        return this;
    }
