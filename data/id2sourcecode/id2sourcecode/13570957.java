    public static void copy(@NotNull final InputStream source, @NotNull final OutputStream destination) {
        try {
            byte[] buffer = new byte[2048];
            int readed = 0;
            while ((readed = source.read(buffer, 0, buffer.length)) > 0) {
                destination.write(buffer, 0, readed);
            }
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            try {
                source.close();
            } catch (IOException e) {
            }
            try {
                destination.close();
            } catch (IOException e) {
            }
        }
    }
