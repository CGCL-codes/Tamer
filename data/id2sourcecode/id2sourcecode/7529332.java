    boolean openMic() {
        bClosePending = false;
        boolean bResult = mic.open(audioFormat.getSamplesPerSecond(), audioFormat.getChannelCount(), audioFormat.getBitsPerSample(), this.iBufferSizeInAtoms * iAtomSize, iBufferCount);
        return bResult;
    }
