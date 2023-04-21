    private void goNoClicks(final PlotContainer currentPlot, final AbstractHistogram hist) {
        final String sep = ", ";
        final String equal = " = ";
        synchronized (cursorBin) {
            cloneClickAndAdd(cursorBin);
            StringBuffer output = new StringBuffer();
            int xCoord = cursorBin.getX();
            if (existsAndIsCalibrated(hist)) {
                if (currentPlot.getDimensionality() == 1) {
                    output.append(ENERGY).append(equal).append(currentPlot.getEnergy(xCoord)).append(sep).append(CHANNEL).append(equal).append(xCoord);
                }
            } else {
                output.append(CHANNEL).append(equal).append(xCoord);
            }
            if (currentPlot.getDimensionality() == 1 && existsAndIsCalibrated(hist)) {
                output = new StringBuffer(ENERGY).append(equal).append(xCoord);
                synchronized (this) {
                    xCoord = currentPlot.getChannel(xCoord);
                    if (xCoord > currentPlot.getSizeX()) {
                        xCoord = currentPlot.getSizeX() - 1;
                    }
                }
                output.append(sep).append(CHANNEL).append(equal).append(xCoord);
            }
            final int rangeToUse = 100;
            final int halfRange = rangeToUse / 2;
            final int channelLow = xCoord - halfRange;
            final int channelHigh = channelLow + rangeToUse;
            currentPlot.expand(Bin.create(channelLow), Bin.create(channelHigh));
            textOut.messageOut(output.toString(), MessageHandler.END);
        }
        auto();
        done();
    }
