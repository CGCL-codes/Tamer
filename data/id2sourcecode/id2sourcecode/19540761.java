    public void setCompressionOption(boolean writeDeflated, boolean readDeflated, ProtocolBandwidthTracker compressionBandwidthTracker) {
        this.writeDeflated = writeDeflated;
        this.readDeflated = readDeflated;
        this.compressionBandwidthTracker = compressionBandwidthTracker;
    }
