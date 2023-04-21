    public DirWriter compact() throws IOException {
        File bakFile = backup();
        DirWriterImpl writer = null;
        try {
            DirReaderImpl reader = new DirReaderImpl(bakFile, new FileImageInputStream(bakFile)).initReader();
            try {
                Dataset fsi = reader.getFileSetInfo();
                writer = new DirWriterImpl(file, new FileImageOutputStream(file), encParam);
                writer.initWriter(fsi.getFileMetaInfo(), fsi.getString(Tags.FileSetID), reader.getDescriptorFile(), fsi.getString(Tags.SpecificCharacterSetOfFileSetDescriptorFile));
                copy(reader, writer);
                writer.commit();
                writer.setAutoCommit(autoCommit);
            } finally {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
        } catch (IOException e) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ignore) {
                }
            }
            file.delete();
            bakFile.renameTo(file);
            throw e;
        }
        bakFile.delete();
        return writer;
    }
