    public void FindTheBandwidthBenchmarkActive() {
        MyLogger.logLn("Starting bandwidth benchmark - active side");
        double normalPacketLoss = findPacketLoss(ASSURED_BANDWIDTH);
        MyLogger.logLn("Normal packet loss is:" + normalPacketLoss);
        long currentBW;
        long lowBW = ASSURED_BANDWIDTH;
        long highBW = MAXIMUM_BANDWIDTH;
        while (lowBW < highBW) {
            currentBW = (lowBW + highBW) / 2;
            MyLogger.logLn("Testing bandwidth " + currentBW + " between " + lowBW + " - " + highBW);
            double currentPacketLoss = findPacketLoss(currentBW);
            if (currentPacketLoss > ABOVE_BANDWIDTH_PACKET_LOSS_RATIO * normalPacketLoss) {
                highBW = currentBW - 1;
                MyLogger.logLn("Decreasing high BW limit to " + highBW);
            } else {
                lowBW = currentBW + 1;
                MyLogger.logLn("Increasing low BW limit to " + lowBW);
            }
        }
        long projectedBandwidth = (lowBW + highBW) / 2;
        MyLogger.logLn("Bandwidth is " + projectedBandwidth + "bps");
        sendInformationFrame_BandwidthBenchmark_BeforeTransfer_Quit();
    }
