    public SoundFrame() {
        super();
        initialize();
        setLocation(CenterType.ON_SCREEN);
        try {
            soundViewerComponent.openAudioFile("test/8bit/test2ch.wav");
            jFilename.setText(soundViewerComponent.getFilename());
            jDuration.setText(Double.toString(soundViewerComponent.getDuration()));
            jChannels.setText(Integer.toString(soundViewerComponent.getChannels()));
            jSampleRate.setText(Double.toString(soundViewerComponent.getSampleRate()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
