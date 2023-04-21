    private static File jarEntryAsFile(JarFile jarFile, JarEntry jarEntry) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            String name = jarEntry.getName().replace('/', '_');
            int i = name.lastIndexOf(".");
            String extension = i > -1 ? name.substring(i) : "";
            File file = File.createTempFile(name.substring(0, name.length() - extension.length()) + ".", extension);
            file.deleteOnExit();
            input = jarFile.getInputStream(jarEntry);
            output = new FileOutputStream(file);
            int readCount;
            byte[] buffer = new byte[4096];
            while ((readCount = input.read(buffer)) != -1) {
                output.write(buffer, 0, readCount);
            }
            return file;
        } finally {
            close(input);
            close(output);
        }
    }
