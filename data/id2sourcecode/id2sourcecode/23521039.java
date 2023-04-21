    protected static synchronized MappedByteBuffer requestMap(FS2BlockProvider requester) throws IOException {
        synchronized (FS2BlockProvider.class) {
            for (int i = 0; i < currentShadowSize; i++) {
                if (currentMapOwners[i] == requester) {
                    lastRequestIndex = i;
                    return centralMaps[i];
                }
            }
            if (currentShadowSize < shadowSize) {
                currentMapOwners[currentShadowSize] = requester;
                FileChannel channel = requester.getFile().getChannel();
                MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, requester.size());
                centralMaps[currentShadowSize] = map;
                lastRequestIndex = currentShadowSize;
                currentShadowSize++;
                return map;
            } else {
                int kickIndex = random.nextInt(shadowSize - 1);
                if (kickIndex == lastRequestIndex) {
                    kickIndex++;
                }
                centralMaps[kickIndex].force();
                currentMapOwners[kickIndex] = requester;
                FileChannel channel = requester.getFile().getChannel();
                MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, requester.size());
                centralMaps[kickIndex] = map;
                lastRequestIndex = kickIndex;
                System.gc();
                return map;
            }
        }
    }
