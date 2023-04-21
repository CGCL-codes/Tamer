    private float getLength(JMEAudioInputStream tmpStream) throws IOException {
        byte copyBuffer[] = new byte[1024 * 4];
        boolean done = false;
        int bytesRead = 0;
        int length = 0;
        while (!done) {
            bytesRead = tmpStream.read(copyBuffer, 0, copyBuffer.length);
            if (bytesRead != -1) length += bytesRead;
            done = (bytesRead != copyBuffer.length || bytesRead < 0);
        }
        int channels = tmpStream.getChannels();
        return (length) / (float) (tmpStream.rate() * tmpStream.getAudioChannels() * 2);
    }
