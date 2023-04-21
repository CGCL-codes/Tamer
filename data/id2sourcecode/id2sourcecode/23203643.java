    public static boolean loadLibraryFromClassPath(String libraryName) throws IOException {
        String platformLibName = System.mapLibraryName(libraryName);
        InputStream in = DynamicLibraryLoader.class.getResourceAsStream(platformLibName);
        if (in == null) return false;
        try {
            File tempFile = File.createTempFile(TEMP_PREFIX, TEMP_SUFFIX);
            FileOutputStream out = new FileOutputStream(tempFile);
            int counter = 0;
            byte[] buffer = new byte[8192];
            while ((counter = in.read(buffer)) != -1) out.write(buffer, 0, counter);
            out.flush();
            out.close();
            tempFile.deleteOnExit();
            String canonicalFileName = tempFile.getCanonicalPath();
            Runtime.getRuntime().load(canonicalFileName);
            return true;
        } finally {
            in.close();
        }
    }
