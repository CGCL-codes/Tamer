    public SvdlibcSparseBinaryFileRowIterator(File matrixFile) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(matrixFile, "r");
        FileChannel fc = raf.getChannel();
        data = fc.map(MapMode.READ_ONLY, 0, fc.size());
        fc.close();
        this.rows = data.getInt();
        this.cols = data.getInt();
        nzEntriesInMatrix = data.getInt();
        curCol = 0;
        entry = 0;
        advance();
    }
