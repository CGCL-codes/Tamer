    private synchronized void decodeMP4(String in, String out) throws Exception {
        WaveFileWriter wav = null;
        try {
            final MP4Container cont = new MP4Container(new RandomAccessFile(in, "r"));
            final Movie movie = cont.getMovie();
            final List<Track> tracks = movie.getTracks(AudioTrack.AudioCodec.AAC);
            if (tracks.isEmpty()) throw new Exception("movie does not contain any AAC track");
            final AudioTrack track = (AudioTrack) tracks.get(0);
            wav = new WaveFileWriter(new File(out), track.getSampleRate(), track.getChannelCount(), track.getSampleSize());
            final Decoder dec = new Decoder(track.getDecoderSpecificInfo());
            Frame frame;
            final SampleBuffer buf = new SampleBuffer();
            while (track.hasMoreFrames()) {
                frame = track.readNextFrame();
                dec.decodeFrame(frame.getData(), buf);
                wav.write(buf.getData());
            }
        } finally {
            if (wav != null) wav.close();
        }
    }
