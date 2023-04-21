    public int read(ALayer l, int offset, int length) throws IOException {
        int channels = audioInputStream.getFormat().getChannels();
        int readLength = audioInputStream.read(buffer, 0, length * channels);
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < readLength / channels; j++) {
                int index = j * channels + i;
                float data = (((int) buffer[index]) & 0xFF);
                if (data > 128) data = -data + 384;
                data -= 128;
                if (data > 0) data = ulawTable[(int) data] * 32768; else data = -ulawTable[Math.abs((int) data)] * 32768;
                l.getChannel(i).setSample(offset + j, data);
            }
        }
        if (readLength >= 0) return readLength / channels; else return readLength;
    }
