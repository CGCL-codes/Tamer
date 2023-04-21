    @NotNull
    public static File getFile(@Nullable final File dir, @NotNull final String fileName) throws IOException {
        final URL url = getResource(dir, fileName);
        final String urlString = url.toString();
        if (urlString.startsWith("file:")) {
            final File file = new File(urlString.substring(5));
            if (file.exists()) {
                return file;
            }
        }
        final File tmpFile = File.createTempFile("gridarta", null);
        tmpFile.deleteOnExit();
        final InputStream inputStream = url.openStream();
        try {
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            try {
                final FileOutputStream outputStream = new FileOutputStream(tmpFile);
                try {
                    final byte[] buf = new byte[65536];
                    while (true) {
                        final int len = bufferedInputStream.read(buf);
                        if (len == -1) {
                            break;
                        }
                        outputStream.write(buf, 0, len);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                bufferedInputStream.close();
            }
        } finally {
            inputStream.close();
        }
        return tmpFile;
    }
