    private void parse(URL url) {
        try {
            InputStream in = url.openStream();
            try {
                ZipInputStream zipStream = new ZipInputStream(in);
                for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
                    if (entry.getName().endsWith(CLASS_ENTRY_SUFFIX)) {
                        annotationParser.parse(classLoader.loadClass(entry.getName()));
                    }
                    zipStream.closeEntry();
                }
            } finally {
                in.close();
            }
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(e);
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }
