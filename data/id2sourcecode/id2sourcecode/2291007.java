    private static void copyResourceToFile(ClassLoader loader, String resource, String file) throws IOException {
        final File out = new File(file);
        out.delete();
        if (!out.exists()) {
            OutputStream outOS = null;
            InputStream inIS = null;
            try {
                inIS = loader.getResourceAsStream(resource);
                if (inIS == null) {
                    throw new FileNotFoundException(resource);
                }
                outOS = new FileOutputStream(out);
                out.deleteOnExit();
                final byte[] buffer = new byte[4096];
                int read;
                while ((read = inIS.read(buffer)) >= 0) {
                    outOS.write(buffer, 0, read);
                }
            } finally {
                Closeables.saveClose(inIS);
                Closeables.saveClose(outOS);
            }
        }
    }
