    private void packageToFile(BuildableBundle builder) {
        try {
            new File(WORKDIR).mkdir();
            final InputStream in = (builder != null) ? bundle.build(builder) : bundle.build();
            final FileOutputStream out = new FileOutputStream(path);
            byte[] buff = new byte[1024];
            for (int read = in.read(buff); read > 0; read = in.read(buff)) {
                out.write(buff, 0, read);
            }
            out.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
