    public void writeData(OutputStream out) throws IOException {
        if (!closed) {
            close();
        }
        if (memoryOutput != null) {
            memoryOutput.writeTo(out);
        }
        if (file != null) {
            FileInputStream fileInput = new FileInputStream(file);
            boolean inputClosed = false;
            try {
                byte[] buffer = new byte[inputBufferLength];
                int read;
                while ((read = fileInput.read(buffer)) > 0) {
                    out.write(buffer, 0, read);
                }
                fileInput.close();
                inputClosed = true;
            } finally {
                if (!inputClosed) {
                    try {
                        fileInput.close();
                    } catch (IOException e) {
                        log.warn("Could not close file input stream", e);
                    }
                }
            }
        }
    }
