    private void pipeStream(InputStream is, OutputStream os) throws IOException {
        synchronized (buffer) {
            try {
                for (int bytesRead = is.read(buffer); bytesRead != -1; bytesRead = is.read(buffer)) os.write(buffer, 0, bytesRead);
                is.close();
                os.close();
            } catch (IOException ioe) {
                try {
                    is.close();
                } catch (Exception ioexc) {
                }
                try {
                    os.close();
                } catch (Exception ioexc) {
                }
                throw ioe;
            }
        }
    }
