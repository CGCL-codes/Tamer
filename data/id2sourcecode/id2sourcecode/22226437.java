    public void copyFileToRAW(File file, String directoryName) throws DirectoryException, FileNotFoundException, IOException, InitializationException {
        directoryName = StringOperations.removeDeviceName(directoryName);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        createFile(directoryName, raf.length());
        ExtendedRandomAccessFile eraf = getRandomAccessFile(directoryName, "rw");
        int read;
        for (; ; ) {
            read = raf.read(sectorBuffer);
            if (read == -1) break;
            eraf.write(sectorBuffer, 0, read);
        }
        raf.close();
        eraf.close();
    }
