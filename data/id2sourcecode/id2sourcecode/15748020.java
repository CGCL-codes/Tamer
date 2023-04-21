    public FileBuffer(File file, int cacheSize) throws FileNotFoundException {
        _file = new RandomAccessFile(file, "rws").getChannel();
        _cache = new LRUCache(cacheSize);
        while (true) {
            try {
                _penultimatePosition = _lastPosition;
                if (!isValid(_lastPosition)) break;
                _lastPosition = readHeader(_lastPosition).next;
                ++_size;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
