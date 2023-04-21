    public static Resource create(URL url) throws IOException {
        ResourceBuilder builder = new ResourceBuilder();
        PrintStream out = builder.getPrintStream();
        URLConnection connection = url.openConnection();
        builder.setContentType(connection.getContentType());
        final InputStream inputStream = connection.getInputStream();
        try {
            byte[] buffer = new byte[32 * 1024];
            int readed = inputStream.read(buffer);
            while (readed != -1) {
                out.write(buffer, 0, readed);
                readed = inputStream.read(buffer);
            }
        } finally {
            inputStream.close();
        }
        return builder.build();
    }
