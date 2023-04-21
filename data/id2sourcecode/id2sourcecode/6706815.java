    public int read(int offset, int length) throws IOException {
        int readLength = line.read(buffer, 0, Math.min(buffer.length, length * channels));
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < readLength / channels; j++) {
                int index = j * channels + i;
                int data = (int) (buffer[index]);
                layer.getChannel(i).setSample(offset + j, data);
            }
        }
        if (readLength >= 0) return readLength / channels; else return readLength;
    }
