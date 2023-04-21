    private void exportOneClass(final Class<?> objectClass) throws StoreException {
        final ClassLoader classLoader = storeAccessForExport.getClassLoader();
        final String className = objectClass.getName();
        final String classResourceName = className.replace('.', '/') + ".class";
        final InputStream input = new BufferedInputStream(classLoader.getResourceAsStream(classResourceName));
        final File outFile = new File(classDirectory, className + ".class");
        try {
            final OutputStream output = new BufferedOutputStream(new FileOutputStream(outFile));
            int read;
            while ((read = input.read()) != -1) {
                output.write(read);
            }
            output.flush();
            output.close();
            input.close();
        } catch (final IOException exception) {
            throw new StoreException(exception);
        }
    }
