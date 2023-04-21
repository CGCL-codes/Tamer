    private void _write(File file) throws BasicIOException {
        if (file.getSize() > 0) {
            ExtendedReader reader = new ExtendedReader(file);
            ExtendedWriter writer = new ExtendedWriter(getWriter(), STREAM_NAME);
            Streams.copy(reader, writer);
            reader.close();
            writer.close();
        }
    }
