        GsmTrack(AudioFormat format, boolean enabled, Time startTime, int numBuffers, int bufferSize, long minLocation, long maxLocation) {
            super(GsmParser.this, format, enabled, GsmParser.this.duration, startTime, numBuffers, bufferSize, GsmParser.this.stream, minLocation, maxLocation);
            double sampleRate = format.getSampleRate();
            int channels = format.getChannels();
            int sampleSizeInBits = format.getSampleSizeInBits();
            float bytesPerSecond;
            float bytesPerFrame;
            float samplesPerFrame;
            long durationNano = this.duration.getNanoseconds();
            if (!((durationNano == Duration.DURATION_UNKNOWN.getNanoseconds()) || (durationNano == Duration.DURATION_UNBOUNDED.getNanoseconds()))) {
                maxFrame = mapTimeToFrame(this.duration.getSeconds());
            }
        }
