    public static InputStream getStream(final String fileName) throws AbstractZemucanException {
        if (fileName == null) {
            throw new FileNotDefinedException();
        }
        InputStream inputStream = null;
        final ClassLoader loader = ClassLoader.getSystemClassLoader();
        if (loader != null) {
            URL url = loader.getResource(fileName);
            if (url == null) {
                url = loader.getResource("/" + fileName);
            }
            if (url != null) {
                try {
                    inputStream = url.openStream();
                } catch (final IOException exception) {
                    throw new CorruptFileException(exception);
                }
            } else {
                final File file = new File(fileName);
                if (file.exists()) {
                    try {
                        inputStream = new FileInputStream(file);
                    } catch (final java.io.FileNotFoundException e) {
                        throw new name.angoca.zemucan.tools.file.FileNotFoundException(fileName, e);
                    }
                } else {
                    throw new name.angoca.zemucan.tools.file.FileNotFoundException(fileName);
                }
            }
        }
        assert inputStream != null;
        return inputStream;
    }
