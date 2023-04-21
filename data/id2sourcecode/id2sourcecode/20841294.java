    public void zip(final File file) throws IOException {
        final String ZIP_EXTENSION = ".zip";
        InputStream is = null;
        ZipOutputStream os = null;
        try {
            os = new ZipOutputStream(new FileOutputStream(file.getAbsolutePath() + ZIP_EXTENSION));
            is = new FileInputStream(file);
            os.putNextEntry(new ZipEntry(file.getName()));
            new StreamReader(is, os).read();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        }
    }
