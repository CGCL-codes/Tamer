    public void openDocumentNotExists(String pFile, IWriterOdfFile write) {
        File file = new File(pFile);
        try {
            readOdf.openDocument(file, write);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
