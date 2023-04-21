    public static void copy(InputStream inputStream, FileOutputStream outputStream) throws IOException {
        try {
            byte[] buffer = new byte[4096];
            int readed = 0;
            while ((readed = inputStream.read(buffer, 0, buffer.length)) > 0) {
                outputStream.write(buffer, 0, readed);
            }
        } finally {
            close(inputStream);
            close(outputStream);
        }
    }
