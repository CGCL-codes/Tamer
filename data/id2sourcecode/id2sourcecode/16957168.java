    public int read(ALayer l, int offset, int length) throws IOException {
        int channels = audioInputStream.getFormat().getChannels();
        int readLength = audioInputStream.read(buffer, 0, length * channels);
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < readLength / channels; j++) {
                int index = j * channels + i;
                int data = ((int) buffer[index] & 0x000000FF);
                l.getChannel(i).setSample(offset + j, data);
            }
        }
        if (readLength >= 0) return readLength / channels; else return readLength;
    }
