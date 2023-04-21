    @Override
    public final long get(OutputStream outputStream) throws ArUnvalidIndexException, ArFileException {
        if (!isReady) {
            throw new ArUnvalidIndexException("Doc is not ready");
        }
        if (outputStream == null) {
            throw new ArUnvalidIndexException("OutputStream is not ready");
        }
        if (inputStream == null) {
            throw new ArFileException("Doc not ready to be read");
        }
        byte[] bytes = new byte[blocksize];
        int read = 0;
        try {
            read = inputStream.read(bytes);
            while (read > 0) {
                outputStream.write(bytes, 0, read);
                position += read;
                read = inputStream.read(bytes);
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                }
                inputStream = null;
            }
            bytes = null;
            throw new ArFileException("Doc is not readable");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                }
                inputStream = null;
            }
        }
        return position;
    }
