    private void loadSourceECGIntoScrollPane(JFrame application, SourceECG sourceECG, JScrollPane scrollPaneOfDisplayedECG, JScrollPane scrollPaneOfAttributeTree, int requestedHeightOfTileInMicroVolts, int requestedHorizontalScalingInMilliMetresPerSecond, int requestedVerticalScalingInMilliMetresPerMilliVolt) {
        Cursor was = application.getCursor();
        application.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        int numberOfChannels = sourceECG.getNumberOfChannels();
        int numberOfSamplesPerChannel = sourceECG.getNumberOfSamplesPerChannel();
        float samplingIntervalInMilliSeconds = sourceECG.getSamplingIntervalInMilliSeconds();
        int nTilesPerColumn = numberOfChannels;
        int nTilesPerRow = 1;
        int timeOffsetInMilliSeconds = 0;
        float requestedHeightOfTileInMilliVolts = (float) (requestedHeightOfTileInMicroVolts) / 1000f;
        float horizontalPixelsPerMilliSecond = (float) (requestedHorizontalScalingInMilliMetresPerSecond) / (1000 * milliMetresPerPixel);
        float verticalPixelsPerMilliVolt = (float) (requestedVerticalScalingInMilliMetresPerMilliVolt) / milliMetresPerPixel;
        float widthOfPixelInMilliSeconds = 1 / horizontalPixelsPerMilliSecond;
        float widthOfSampleInPixels = samplingIntervalInMilliSeconds / widthOfPixelInMilliSeconds;
        int maximumWidthOfRowInSamples = numberOfSamplesPerChannel * nTilesPerRow;
        int maximumWidthOfRowInPixels = (int) (maximumWidthOfRowInSamples * widthOfSampleInPixels);
        float heightOfPixelInMilliVolts = 1 / verticalPixelsPerMilliVolt;
        int wantECGPanelheight = (int) (nTilesPerColumn * requestedHeightOfTileInMilliVolts / heightOfPixelInMilliVolts);
        ECGPanel pg = new ECGPanel(sourceECG.getSamples(), numberOfChannels, numberOfSamplesPerChannel, sourceECG.getChannelNames(), nTilesPerColumn, nTilesPerRow, samplingIntervalInMilliSeconds, sourceECG.getAmplitudeScalingFactorInMilliVolts(), horizontalPixelsPerMilliSecond, verticalPixelsPerMilliVolt, timeOffsetInMilliSeconds, sourceECG.getDisplaySequence(), maximumWidthOfRowInPixels, wantECGPanelheight);
        pg.setPreferredSize(new Dimension(maximumWidthOfRowInPixels, wantECGPanelheight));
        scrollPaneOfDisplayedECG.setViewportView(pg);
        application.setCursor(was);
    }
