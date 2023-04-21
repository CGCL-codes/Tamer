    private AudioData load(InputStream in, boolean readStream, boolean streamCache) throws IOException {
        if (readStream && streamCache) {
            oggStream = new CachedOggStream(in);
        } else {
            oggStream = new UncachedOggStream(in);
        }
        Collection<LogicalOggStream> streams = oggStream.getLogicalStreams();
        loStream = streams.iterator().next();
        vorbisStream = new VorbisStream(loStream);
        streamHdr = vorbisStream.getIdentificationHeader();
        if (!readStream) {
            AudioBuffer audioBuffer = new AudioBuffer();
            audioBuffer.setupFormat(streamHdr.getChannels(), 16, streamHdr.getSampleRate());
            audioBuffer.updateData(readToBuffer());
            return audioBuffer;
        } else {
            AudioStream audioStream = new AudioStream();
            audioStream.setupFormat(streamHdr.getChannels(), 16, streamHdr.getSampleRate());
            float streamDuration = computeStreamDuration();
            audioStream.updateData(readToStream(oggStream.isSeekable(), streamDuration), streamDuration);
            return audioStream;
        }
    }
