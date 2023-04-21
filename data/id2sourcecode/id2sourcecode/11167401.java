    public void initialize(int size) {
        this.size = size;
        last = (int) (Math.random() * size);
        if (last >= size) last = 0;
        int segmentSize = size;
        int segmentCount = 1;
        while (segmentSize % 2 == 0) {
            segmentCount <<= 1;
            segmentSize >>= 1;
        }
        increment = (segmentSize - 1) >> 1;
        if (segmentCount > 2) {
            increment += (segmentCount >> 1) * segmentSize;
        }
    }
