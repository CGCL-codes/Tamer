    protected final void copyPathToPath(InputStream inputStream, OutputStream outputStream) throws ArFileException {
        try {
            int read;
            byte[] bytes = new byte[ArConstants.BUFFERSIZEDEFAULT];
            while ((read = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, read);
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (IOException e) {
            throw new ArFileException("Error while copying", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
                outputStream = null;
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
                inputStream = null;
            }
        }
    }
