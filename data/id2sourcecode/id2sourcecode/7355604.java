    public MemoryMappedFragmentDataStore(File file, MemoryMappedFileRange fragmentInfomemoryMappedFileRange, MemoryMappedFileRange mateInfoMemoryMappedFileRange, Frg2Parser parser) throws FileNotFoundException {
        this.fragmentInfomemoryMappedFileRange = fragmentInfomemoryMappedFileRange;
        this.mateInfoMemoryMappedFileRange = mateInfoMemoryMappedFileRange;
        this.parser = parser;
        this.fragFile = new RandomAccessFile(file, "r").getChannel();
    }
