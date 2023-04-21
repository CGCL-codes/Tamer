    protected void extractFile(TarEntryHeader header) throws IOException, TarMalformatException {
        if (header.getDataSize() < 1) {
            throw new TarMalformatException(RB.data_size_unknown.getString());
        }
        int readNow;
        int readBlocks = (int) (header.getDataSize() / 512L);
        int modulus = (int) (header.getDataSize() % 512L);
        File newFile = header.generateFile();
        if (!newFile.isAbsolute()) {
            newFile = (extractBaseDir == null) ? newFile.getAbsoluteFile() : new File(extractBaseDir, newFile.getPath());
        }
        File parentDir = newFile.getParentFile();
        if (newFile.exists()) {
            if (mode != TarReader.OVERWRITE_MODE) {
                throw new IOException(RB.extraction_exists.getString(newFile.getAbsolutePath()));
            }
            if (!newFile.isFile()) {
                throw new IOException(RB.extraction_exists_notfile.getString(newFile.getAbsolutePath()));
            }
        }
        if (parentDir.exists()) {
            if (!parentDir.isDirectory()) {
                throw new IOException(RB.extraction_parent_not_dir.getString(parentDir.getAbsolutePath()));
            }
            if (!parentDir.canWrite()) {
                throw new IOException(RB.extraction_parent_not_writable.getString(parentDir.getAbsolutePath()));
            }
        } else {
            if (!parentDir.mkdirs()) {
                throw new IOException(RB.extraction_parent_mkfail.getString(parentDir.getAbsolutePath()));
            }
        }
        int fileMode = header.getFileMode();
        FileOutputStream outStream = new FileOutputStream(newFile);
        try {
            newFile.setExecutable(false, false);
            newFile.setReadable(false, false);
            newFile.setWritable(false, false);
            newFile.setExecutable(((fileMode & 0100) != 0), true);
            newFile.setReadable((fileMode & 0400) != 0, true);
            newFile.setWritable((fileMode & 0200) != 0, true);
            while (readBlocks > 0) {
                readNow = (readBlocks > archive.getReadBufferBlocks()) ? archive.getReadBufferBlocks() : readBlocks;
                archive.readBlocks(readNow);
                readBlocks -= readNow;
                outStream.write(archive.readBuffer, 0, readNow * 512);
            }
            if (modulus != 0) {
                archive.readBlock();
                outStream.write(archive.readBuffer, 0, modulus);
            }
            outStream.flush();
        } finally {
            try {
                outStream.close();
            } finally {
                outStream = null;
            }
        }
        newFile.setLastModified(header.getModTime() * 1000);
        if (newFile.length() != header.getDataSize()) {
            throw new IOException(RB.write_count_mismatch.getString(Long.toString(header.getDataSize()), newFile.getAbsolutePath(), Long.toString(newFile.length())));
        }
    }
