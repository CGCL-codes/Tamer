    public float[] read_frames_float(int len) throws IOException {
        return read_items_float(len * info.getChannels());
    }
