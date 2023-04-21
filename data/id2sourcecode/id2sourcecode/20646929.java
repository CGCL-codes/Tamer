    public iviSimPluginShmIfc(File shm_file) {
        try {
            d_file = new RandomAccessFile(shm_file, "r");
            FileChannel fc = d_file.getChannel();
            d_mapping = fc.map(FileChannel.MapMode.READ_ONLY, 0, MapSizeIdx + 4);
            if (readInt(HostBigEndianIdx) != 0) {
                d_isBigEndian = true;
            } else {
                d_isBigEndian = false;
            }
            int mapSize = readInt(MapSizeIdx);
            d_mapping = fc.map(FileChannel.MapMode.READ_ONLY, 0, mapSize);
        } catch (IOException e) {
            System.out.println("Failed to open file \"" + shm_file.getAbsolutePath() + "\": " + e.getMessage());
        }
    }
