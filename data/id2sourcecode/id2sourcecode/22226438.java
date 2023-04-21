    public void copyFileToOriginalFileSystem(String rawDirectoryName, File originalFile) throws DirectoryException, IOException, InitializationException {
        String directoryName = StringOperations.removeDeviceName(rawDirectoryName);
        RandomAccessFile raf = new RandomAccessFile(originalFile, "rw");
        ExtendedRandomAccessFile eraf = getRandomAccessFile(directoryName, "r");
        int read;
        for (; ; ) {
            read = eraf.read(sectorBuffer);
            if (read == -1) break;
            raf.write(sectorBuffer, 0, read);
        }
        raf.close();
        eraf.close();
    }
