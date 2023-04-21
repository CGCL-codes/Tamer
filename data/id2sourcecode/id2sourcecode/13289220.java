    private final boolean isGZipped(URL url) throws lvThrowable {
        boolean isZipped = false;
        try {
            InputStream in = url.openStream();
            byte header[] = new byte[2];
            in.read(header);
            if (header[0] == 31 && header[1] == -117) isZipped = true;
            in.close();
        } catch (IOException e) {
            Err().Assert(false, e.getMessage());
        }
        return isZipped;
    }
