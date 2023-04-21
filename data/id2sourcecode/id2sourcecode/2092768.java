    public void loadPDF(InputStream is) {
        if (tmpFile != null) {
            tmpFile.delete();
        }
        try {
            tmpFile = File.createTempFile("adempiere", ".pdf");
            tmpFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            final OutputStream os = new FileOutputStream(tmpFile);
            try {
                final byte[] buffer = new byte[32768];
                for (int read; (read = is.read(buffer)) != -1; ) {
                    os.write(buffer, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadPDF(tmpFile.getAbsolutePath());
    }
