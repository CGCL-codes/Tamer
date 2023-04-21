    private Channel getChannel(int row) {
        if (_channelModel == null) return null;
        switch(row) {
            case WAVEFORM_ROW:
                return _channelModel.getChannel();
            case DELAY_ROW:
                return _channelModel.getDelayChannel();
            case SAMPLE_PERIOD_ROW:
                return _channelModel.getSamplePeriodChannel();
            default:
                return null;
        }
    }
