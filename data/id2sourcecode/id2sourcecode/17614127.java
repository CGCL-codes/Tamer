    public static void main(String arg[]) {
        try {
            SourceECG sourceECG = null;
            BinaryInputStream i = new BinaryInputStream(new BufferedInputStream(new FileInputStream(arg[1])), false);
            int nTilesPerColumn = 0;
            int nTilesPerRow = 0;
            float timeOffsetInMilliSeconds = 0;
            if (arg.length == 9 && arg[0].toUpperCase().equals("RAW")) {
                int numberOfChannels = Integer.parseInt(arg[2]);
                int nSamplesPerChannel = Integer.parseInt(arg[3]);
                nTilesPerColumn = Integer.parseInt(arg[4]);
                nTilesPerRow = Integer.parseInt(arg[5]);
                float samplingIntervalInMilliSeconds = Float.parseFloat(arg[6]);
                float amplitudeScalingFactorInMilliVolts = Float.parseFloat(arg[7]);
                timeOffsetInMilliSeconds = Float.parseFloat(arg[8]);
                sourceECG = new RawSourceECG(i, numberOfChannels, nSamplesPerChannel, samplingIntervalInMilliSeconds, amplitudeScalingFactorInMilliVolts, true);
            } else if (arg.length == 5) {
                nTilesPerColumn = Integer.parseInt(arg[2]);
                nTilesPerRow = Integer.parseInt(arg[3]);
                timeOffsetInMilliSeconds = Float.parseFloat(arg[4]);
                if (arg[0].toUpperCase().equals("SCPECG")) {
                    sourceECG = new SCPSourceECG(i, true);
                } else if (arg[0].toUpperCase().equals("DICOM")) {
                    sourceECG = new DicomSourceECG(i);
                }
            }
            float milliMetresPerPixel = (float) (25.4 / 72);
            float horizontalPixelsPerMilliSecond = 25 / (1000 * milliMetresPerPixel);
            float verticalPixelsPerMilliVolt = 10 / (milliMetresPerPixel);
            ECGPanel pg = new ECGPanel(sourceECG.getSamples(), sourceECG.getNumberOfChannels(), sourceECG.getNumberOfSamplesPerChannel(), sourceECG.getChannelNames(), nTilesPerColumn, nTilesPerRow, sourceECG.getSamplingIntervalInMilliSeconds(), sourceECG.getAmplitudeScalingFactorInMilliVolts(), horizontalPixelsPerMilliSecond, verticalPixelsPerMilliVolt, timeOffsetInMilliSeconds, sourceECG.getDisplaySequence(), 800, 400);
            pg.setPreferredSize(new Dimension(800, 400));
            String title = sourceECG.getTitle();
            ApplicationFrame app = new ApplicationFrame(title == null ? "ECG Panel" : title);
            app.getContentPane().add(pg);
            app.pack();
            app.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
