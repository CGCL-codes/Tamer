    private static void copyFileToTemp(String file, String tempDir) {
        InputStream is = OntologizerCore.class.getResourceAsStream(file);
        if (is == null) return;
        try {
            byte[] buf = new byte[8192];
            int read;
            File destFile = new File(tempDir, new File(file).getName());
            BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(destFile));
            while ((read = is.read(buf)) > 0) dest.write(buf, 0, read);
            dest.close();
            destFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
