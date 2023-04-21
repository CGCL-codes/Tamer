    private void map() throws IOException {
        synchronized (lock) {
            FileChannel channel = istream.getChannel();
            if (channel.isOpen()) {
                if (parseBuffer == null || channel.size() > mappedStart) {
                    if (parseBuffer != null) {
                        clean(parseBuffer);
                    }
                    if (readBuffer != null) {
                        clean(readBuffer);
                    }
                    mappedStart += position;
                    parseBuffer = channel.map(FileChannel.MapMode.READ_ONLY, mappedStart, channel.size() - mappedStart);
                    readBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                    position = 0;
                }
            }
        }
    }
