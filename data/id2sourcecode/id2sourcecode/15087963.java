    static void processZipFile(URL url, PrintWriter writer) throws IOException, NoSuchFieldException, IllegalAccessException {
        final ZipInputStream zipStream = new ZipInputStream(url.openStream());
        ZipEntry entry;
        while ((entry = zipStream.getNextEntry()) != null) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                final byte[] b = unzipEntry(zipStream);
                processClass(url.toString() + "#" + entry.getName(), new ByteArrayInputStream(b), writer);
                processFieldsAndMethods(url.toString() + "#" + entry.getName(), new ByteArrayInputStream(b), writer);
            }
        }
    }
