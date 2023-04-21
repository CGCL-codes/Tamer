    @Override
    public void open() throws IOException {
        file = File.createTempFile(System.getProperty("user.name"), System.getProperty("application.simplename"));
        final InputStream input = url.openStream();
        try {
            final OutputStream output = new FileOutputStream(file);
            try {
                int bytes = input.read(buffer);
                while (-1 != bytes) {
                    output.write(buffer, 0, bytes);
                    output.flush();
                    bytes = input.read(buffer);
                }
            } finally {
                output.close();
            }
        } finally {
            input.close();
        }
        source = new FileSource(IndexEntryBuilder.newBuilder().setSource(url), file);
        source.open();
    }
