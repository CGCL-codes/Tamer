    private static ByteArrayResource toByteArrayResource(final Resource resource) throws IOException {
        final InputStream is = resource.openAsStream();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] tmp = new byte[8192];
            int read;
            while ((read = is.read(tmp)) != -1) {
                baos.write(tmp, 0, read);
            }
            return new ByteArrayResource(baos.toByteArray(), resource.getName());
        } finally {
            is.close();
        }
    }
