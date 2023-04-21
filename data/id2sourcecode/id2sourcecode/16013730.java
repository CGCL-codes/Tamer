    public Sample(File f) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.file = f;
        setName(f.getName());
        setColor(ColorScheme.DEFAULT_SAMPLE_COLOR);
        readSampleData(f);
        addStartAndEndSnapPoints();
        for (int channel = 0; channel < fsb.getChannelCount(); channel++) {
            outputMap.addMapping(channel, channel);
        }
    }
