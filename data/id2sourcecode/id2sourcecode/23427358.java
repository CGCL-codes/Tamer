    private MipMap(String filename) throws IOException {
        if (currentDirectory != null) filename = currentDirectory + File.separator + filename;
        FileInputStream fis = new FileInputStream(filename);
        FileChannel fc = fis.getChannel();
        data = fc.map(FileChannel.MapMode.READ_ONLY, 0L, fc.size());
        size = data.getInt();
        sMode = Mode.getMode(data.get());
        tMode = Mode.getMode(data.get());
        levels = 1;
        int s = size;
        while (s > 1) {
            levels++;
            s /= 2;
        }
        levelOffsets = new int[levels];
        levelSizes = new int[levels];
        s = size;
        levelOffsets[0] = 6;
        levelSizes[0] = s;
        for (int i = 1; i < levels; i++) {
            levelOffsets[i] = levelOffsets[i - 1] + s * s * 4;
            s /= 2;
            levelSizes[i] = s;
        }
    }
