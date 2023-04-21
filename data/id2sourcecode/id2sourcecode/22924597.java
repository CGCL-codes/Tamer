    public static String loadPage(final URL url) {
        try {
            final StringBuilder result = new StringBuilder();
            final byte[] buffer = new byte[BotUtil.BUFFER_SIZE];
            int length;
            final InputStream is = url.openStream();
            do {
                length = is.read(buffer);
                if (length >= 0) {
                    result.append(new String(buffer, 0, length));
                }
            } while (length >= 0);
            return result.toString();
        } catch (final IOException e) {
            EncogLogging.log(e);
            throw new BotError(e);
        }
    }
