    public static void downloadPage(final URL url, final File file) {
        try {
            final byte[] buffer = new byte[BotUtil.BUFFER_SIZE];
            int length;
            final FileOutputStream fos = new FileOutputStream(file);
            final InputStream is = url.openStream();
            do {
                length = is.read(buffer);
                if (length >= 0) {
                    fos.write(buffer, 0, length);
                }
            } while (length >= 0);
            fos.close();
        } catch (final IOException e) {
            EncogLogging.log(e);
            throw new BotError(e);
        }
    }
