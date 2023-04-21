    public synchronized int getRemainingPlayTime() {
        int bytesPerFrame = dataLine.getFormat().getChannels() * 2;
        int framesPlayed = dataLine.available() / bytesPerFrame;
        int framesTotal = dataLine.getBufferSize() / bytesPerFrame;
        int framesNotYetPlayed = framesTotal - framesPlayed;
        return framesNotYetPlayed * 1000 / framesTotal;
    }
