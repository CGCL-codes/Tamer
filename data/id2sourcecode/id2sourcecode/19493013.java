    public int read(int offset, int length) throws IOException {
        int readLength = line.read(buffer, 0, Math.min(buffer.length, length * channels));
        for (int i = 0; i < readLength / channels; i++) {
            for (int j = 0; j < channels; j++) {
                int index = i * channels + j;
                int data = (int) (buffer[index]);
                layer.getChannel(j).setSample(offset + i, data);
            }
        }
        if (readLength >= 0) return readLength / channels; else return readLength / channels;
    }
