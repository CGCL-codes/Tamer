    public int[] read_frames_int(int len) throws IOException {
        return read_items_int(len * info.getChannels());
    }
