    private void addFileEntry(FileInputStream entryFile) throws Exception {
        int len;
        while ((len = entryFile.read(buffer)) != -1) zipf.write(buffer, 0, len);
        entryFile.close();
    }
