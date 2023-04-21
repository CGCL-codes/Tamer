    public void loadFile() {
        try {
            fin = new RandomAccessFile(file, "r");
            in = fin.getChannel();
            initializeSegments();
            int tops = 0;
            if (positions.length > 1) {
                tops = MAX_SEGMENT_SIZE;
            } else {
                tops = last_chunk;
            }
            if (positions.length == 0 || tops == 0) {
                return;
            }
            mbb = in.map(FileChannel.MapMode.READ_ONLY, positions[0], tops);
            mbb.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
