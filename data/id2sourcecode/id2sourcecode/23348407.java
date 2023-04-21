    private void init(File moFile) {
        try {
            String key = moFile.getAbsolutePath();
            GettextResource instance = cachedGettextResource.get(key);
            if (instance != null) {
                copyInstance(instance);
                return;
            }
            readOffsetsAndLenghts(moFile);
            logOffsetAndLenghtInfo();
            FileChannel channel = null;
            try {
                channel = new RandomAccessFile(moFile, "r").getChannel();
                for (int i = 0; i < n; i++) {
                    msgidByteBuffers[i] = channel.map(MapMode.READ_ONLY, oo_offset[i], oo_length[i]);
                    msgstrByteBuffers[i] = channel.map(MapMode.READ_ONLY, tt_offset[i], tt_length[i]);
                }
            } finally {
                if (channel != null) {
                    channel.close();
                }
            }
            releaseOffsetAndLenghtArrays();
            setCharset();
            cachedGettextResource.put(key, this);
            logMessages();
        } catch (IOException e) {
            throw new IOError(e);
        }
    }
