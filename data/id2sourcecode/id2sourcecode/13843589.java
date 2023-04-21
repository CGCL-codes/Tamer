    public static void loadLibrary(String library) {
        String filename = System.mapLibraryName(library);
        String fullFilename = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + filename;
        try {
            System.load(fullFilename);
        } catch (UnsatisfiedLinkError err2) {
            try {
                InputStream is = LibraryLoader.class.getClassLoader().getResourceAsStream(filename);
                if (is == null) {
                    throw new IOException(filename + " not found in the jar file (classpath)");
                }
                byte[] buffer = new byte[4096];
                OutputStream os = new FileOutputStream(fullFilename);
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.close();
                is.close();
                new File(fullFilename).setExecutable(true, false);
                System.load(fullFilename);
            } catch (IOException ioe) {
                throw new RuntimeException("Unable to extract native library: " + library, ioe);
            }
        }
    }
