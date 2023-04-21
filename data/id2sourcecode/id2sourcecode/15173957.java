        MappedAccess(ORandomAccess underlying, long initialSize) throws IOException {
            randomAccess = underlying;
            channel = underlying.getChannel();
            if (initialSize <= 0L) {
                initialSize = channel.size();
            }
            if (initialSize < MINIMUM_MAPPED_SIZE) {
                initialSize = MINIMUM_MAPPED_SIZE;
            }
            currentMax = initialSize;
            buffer = channel.map(MapMode.READ_WRITE, 0L, currentMax);
        }
