    public static void initInstance(File committedDir) throws IOException {
        File dir = new File(committedDir, FileConventions.toUnitDirname(0));
        Store.validator().isTrue(dir.mkdir(), "failed to create initial unit directory");
        UnitDir unit = new UnitDir(dir);
        File indexFile = unit.getIndexFile();
        File entryFile = unit.getEntryFile();
        FileChannel indexChannel = new RandomAccessFile(indexFile, "rw").getChannel();
        FileChannel entryChannel = new RandomAccessFile(entryFile, "rw").getChannel();
        BaseSegment.writeNewSegment(indexChannel, entryChannel, 0, Word.INT).close();
    }
