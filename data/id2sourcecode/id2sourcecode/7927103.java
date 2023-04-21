    @Override
    public CVCTChannel getChannelAt(int index) {
        if (index < 0 || index >= channels.size()) return null;
        return channels.get(index);
    }
