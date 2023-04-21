    public int write(int length) throws IOException {
        int factor = audioFormat.getSampleSizeInBits() / 8 * audioFormat.getChannels();
        int n = audioInputStream.read(data, 0, Math.min(length * factor, data.length));
        if (n < 0) return -1;
        line.write(data, 0, n);
        return n / factor;
    }
