    public void close() throws IOException {
        if (zipOut_ != null) {
            if (dbFile_ != null && cont_ != null) {
                cont_.close();
                final ZipEntry entry = new ZipEntry(getDbEntryName());
                entry.setComment("Donnees concernant le style et les calculs interm�diaires");
                zipOut_.putNextEntry(entry);
                CtuluLibFile.copyStream(new FileInputStream(dbFile_), zipOut_, true, false);
                zipOut_.closeEntry();
                dbFile_.delete();
            }
            zipOut_.close();
        }
    }
