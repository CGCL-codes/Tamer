    private int getChannelCount() {
        if (current != null) return current.getFloatSampleBuffer().getChannelCount(); else return 2;
    }
