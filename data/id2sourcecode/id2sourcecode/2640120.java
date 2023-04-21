    protected String exportBinaryProperty(Property pProperty, JB_Options pOptions) throws RepositoryException, UnsupportedEncodingException, FileNotFoundException, IOException {
        File exportDir = pOptions.getExportDirectory();
        File binFile = new File(exportDir, pOptions.getNextBinaryExportFilename());
        FileOutputStream binFos = new FileOutputStream(binFile);
        try {
            InputStream is = pProperty.getStream();
            byte[] buffer = new byte[50000];
            int read;
            while ((read = is.read(buffer)) != -1) {
                binFos.write(buffer, 0, read);
            }
        } finally {
            binFos.close();
        }
        return binFile.getName();
    }
