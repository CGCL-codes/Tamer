    public int getChannels() {
        byte[] data = getData();
        return data[2] & 0x07;
    }
