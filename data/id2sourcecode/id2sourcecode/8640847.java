    public static SeekableInput replaceInputStreamWithSeekableInput(InputStream in) {
        if (in instanceof SeekableInput) return (SeekableInput) in;
        SeekableInput sin = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            while (true) {
                int read = in.read();
                if (read < 0) break;
                out.write(read);
            }
            in.close();
            out.flush();
            out.close();
            byte[] data = out.toByteArray();
            sin = new SeekableByteArrayInputStream(data);
        } catch (IOException ioe) {
            logger.log(Level.FINE, "Problem getting debug string");
        }
        return sin;
    }
