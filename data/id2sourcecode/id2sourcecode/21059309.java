    public EEGDataProvider() {
        EEGAcquisitionController.getInstance().getChannelSampleGenerator().addSampleListener(this, new int[] { 2 });
    }
