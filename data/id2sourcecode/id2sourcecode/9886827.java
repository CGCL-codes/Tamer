    private static void receivedDataStream(int channel, message msg) {
        int[] data = msg.dataInt;
        if (myFileWriterThread != null) myFileWriterThread.writeToFile(hexUtil.byteToHexString(msg.rawMessage));
        if (channel == 0 && !myGui.jCheckBox_channel0DisplayOrNot.isSelected()) return;
        if (channel == 1 && !myGui.jCheckBox_channel1DisplayOrNot.isSelected()) {
            if (keepAquisitionRunning == true) doAquisition();
            return;
        }
        double dataLenMultiplier = (double) (data.length - 1) / (double) 10;
        if (xCoordinates == null) {
            xCoordinates = new double[data.length];
            for (int i = 0; i < data.length; i++) xCoordinates[i] = ((double) i / dataLenMultiplier) - 5.0;
        }
        if (xCoordinates.length != data.length) {
            logger.logInfo("Data with new Resolution received");
            xCoordinates = null;
            xCoordinates = new double[data.length];
            for (int i = 0; i < data.length; i++) xCoordinates[i] = ((double) i / dataLenMultiplier) - 5.0;
        }
        double xOffsetMultiplier = (double) AdcSamplesPerFrameValue / OffsetXmax;
        if (channel == 0) {
            int positionDiff = 0;
            synchronized (Main.OffsetXLockCh0) {
                positionDiff = (int) (OffsetXCh0 * xOffsetMultiplier * positionXOffsetMultiplier);
            }
            synchronized (Main.yValuesCh0) {
                if (yValuesCh0.length == 0) {
                    yValuesCh0 = null;
                    yValuesCh0 = new double[data.length];
                }
                if (yValuesCh0.length != data.length) {
                    yValuesCh0 = null;
                    yValuesCh0 = new double[data.length];
                }
                int position = 0;
                for (int i = 0; i < data.length; i++) {
                    yValuesCh0[i] = 0;
                }
                for (int i = 0; i < data.length; i++) {
                    position = i + positionDiff;
                    if (position >= 0 && position < data.length) yValuesCh0[position] = ((double) data[i] / (double) 2048);
                }
            }
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    myGui.updateDataCh0();
                }
            });
        } else if (channel == 1) {
            int positionDiff = 0;
            synchronized (Main.OffsetXLockCh1) {
                positionDiff = (int) (OffsetXCh1 * xOffsetMultiplier * positionXOffsetMultiplier);
            }
            synchronized (Main.yValuesCh1) {
                if (yValuesCh1.length == 0) {
                    yValuesCh1 = null;
                    yValuesCh1 = new double[data.length];
                }
                if (yValuesCh1.length != data.length) {
                    yValuesCh1 = null;
                    yValuesCh1 = new double[data.length];
                }
                int position = 0;
                for (int i = 0; i < data.length; i++) {
                    yValuesCh1[i] = 0;
                }
                for (int i = 0; i < data.length; i++) {
                    position = i + positionDiff;
                    if (position >= 0 && position < data.length) yValuesCh1[position] = ((double) data[i] / (double) 2048);
                }
                if (AdcCounterReceivedInChannel1) {
                    for (int j = 0; j < data.length; j++) logger.logDebug("AdcCounterReceivedInChannel1 " + j + "\t" + data[j] + " Cycles for single Aquisition");
                }
            }
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    myGui.updateDataCh1();
                }
            });
        }
        if (checkUndersampling) {
            synchronized (myUnderSamplingCheckerThread.dataArray) {
                if (myUnderSamplingCheckerThread.dataArray.length != data.length) myUnderSamplingCheckerThread.dataArray = new int[data.length];
                for (int i = 0; i < data.length; i++) myUnderSamplingCheckerThread.dataArray[i] = data[i];
                myUnderSamplingCheckerThread.channel = channel;
            }
            synchronized (myUnderSamplingCheckerThread) {
                myUnderSamplingCheckerThread.notify();
            }
        }
        if (channel == 1 && keepAquisitionRunning == true) doAquisition();
    }
