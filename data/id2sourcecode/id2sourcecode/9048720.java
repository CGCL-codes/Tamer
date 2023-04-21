    public WaveDataSource(String filename) {
        sr = new JSndWaveReader();
        try {
            sr.open(filename);
            info = sr.getInfo();
            int len = info.getFrames() * info.getChannels();
            v = sr.read_items_float(len / 2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
