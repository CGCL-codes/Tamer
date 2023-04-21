    public static String getString(final InputStream is, final String charEncoding) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte ba[] = new byte[8192];
            int read = is.read(ba);
            while (read > -1) {
                out.write(ba, 0, read);
                read = is.read(ba);
            }
            return out.toString(charEncoding);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
