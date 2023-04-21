    public IndexedFragmentDataStore(File file, IndexedFileRange fragmentInfoIndexFileRange, IndexedFileRange mateInfoIndexFileRange, Frg2Parser parser) throws FileNotFoundException {
        this.fragmentInfoIndexFileRange = fragmentInfoIndexFileRange;
        this.mateInfoIndexFileRange = mateInfoIndexFileRange;
        this.parser = parser;
        this.fragFile = new RandomAccessFile(file, "r").getChannel();
    }
