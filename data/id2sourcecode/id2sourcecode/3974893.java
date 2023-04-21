    void transcodeMPEGToRaw(Processor p) {
        TrackControl tc[] = p.getTrackControls();
        AudioFormat afmt;
        for (int i = 0; i < tc.length; i++) {
            if (tc[i].getFormat() instanceof VideoFormat) tc[i].setFormat(new VideoFormat(VideoFormat.JPEG)); else if (tc[i].getFormat() instanceof AudioFormat) {
                afmt = (AudioFormat) tc[i].getFormat();
                tc[i].setFormat(new AudioFormat(AudioFormat.LINEAR, afmt.getSampleRate(), afmt.getSampleSizeInBits(), afmt.getChannels()));
            }
        }
    }
