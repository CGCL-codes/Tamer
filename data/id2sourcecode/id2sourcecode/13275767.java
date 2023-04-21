    public static final InputStream streamSeek(InputStream is, long pos, long size, String URI, ZipEntry entry, int type) throws IOException {
        if (is == null) return is;
        if (size > 0) {
            try {
                long available = is.available();
                long guesspos = size - available;
                if (guesspos > 0 && guesspos <= pos) {
                    long skipped = 0;
                    long mustskip = pos - guesspos;
                    while (skipped < mustskip) skipped += is.skip(mustskip - skipped);
                    return is;
                }
            } catch (Exception e) {
            }
        }
        if (is instanceof FileInputStream) {
            try {
                ((FileInputStream) is).getChannel().position(pos);
                return is;
            } catch (IOException e) {
                is.close();
                is = createInputStreamFromURI(URI, null, 1);
                is.skip(pos);
                return is;
            }
        }
        if (is instanceof ZipInputStream) {
            is.close();
            is = createInputStreamFromURI(URI, entry, type);
            is.skip(pos);
            return is;
        }
        try {
            URL u = new URL(URI);
            InputStream nis = u.openStream();
            nis.skip(pos);
            is.close();
            return nis;
        } catch (Exception e) {
        }
        return is;
    }
