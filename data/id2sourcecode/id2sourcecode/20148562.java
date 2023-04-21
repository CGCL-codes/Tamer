    private boolean read() {
        try {
            if (is.available() < 0) {
                streamComplete = true;
                return true;
            }
            if (!async && is.available() < 1) {
                streamComplete = true;
                return true;
            }
            if (is.available() < 1) return false;
            {
                int read = is.read(buffer);
                if (read < 1) {
                    setError("unexpected stream closed");
                    return true;
                }
                baos.write(buffer, 0, read);
                bytes_read += read;
            }
            return true;
        } catch (IOException e) {
            setError(e.getMessage());
            return true;
        }
    }
