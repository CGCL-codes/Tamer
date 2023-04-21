    public SampleApp() {
        eegAcquisitionController = EEGAcquisitionController.getInstance();
        IRawSampleGenerator sampleGenerator = eegAcquisitionController.getChannelSampleGenerator();
        SampleListener twoChannelListener = new SampleListener("Channel One and Two");
        sampleGenerator.addSampleListener(twoChannelListener, new int[] { 1, 2 });
        SampleListener oneChannelListener = new SampleListener("Channel One");
        sampleGenerator.addSampleListener(oneChannelListener, new int[] { 1 });
    }
