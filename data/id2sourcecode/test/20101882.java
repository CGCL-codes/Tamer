    public double bestPerformence(Iperf iperf, int bufferLen) throws Exception {
        double actualRate;
        double bestThroughput = minRate;
        int index = 0;
        actualRate = maxRate;
        while (true) {
            ++index;
            if (maxRate - minRate < stopAt) {
                break;
            }
            iperf.startServers(EnumIperfL4Protocol.UDP, bufferLen, true);
            iperf.startClient(iperf.server.getHost(), EnumIperfL4Protocol.UDP, EnumIperfTransmitMode.CONT, 10, bufferLen, actualRate + "M", 40000, true);
            iperf.joinIperf();
            String textAginst = iperf.server.getTestAgainstObject().toString();
            IperfFile iperFile = new IperfFile(textAginst);
            iperFile.buildIperfData(EnumIperfDataType.AVERAGE);
            report.startReport("iperf index" + index, "iperf test");
            report.addProperty("Client", iperf.client.getHost());
            report.addProperty("Server", iperf.server.getHost());
            report.addProperty("Expected throughput", String.valueOf(actualRate));
            report.addProperty("buffer length", String.valueOf(bufferLen));
            double rateIGet = Double.parseDouble(iperFile.returnValue(EnumIperfCounterType.THROUGHPUT));
            rateIGet = doubleFormater(rateIGet, 2);
            report.addProperty("Actual throughput", String.valueOf(rateIGet));
            if ((rateIGet >= (actualRate - tolerance)) && (!Double.isNaN(rateIGet))) {
                bestThroughput = rateIGet;
                minRate = actualRate;
                report.addProperty("Result", "Pass");
            } else {
                maxRate = actualRate;
                report.addProperty("Result", "Fail");
            }
            actualRate = (minRate + maxRate) / 2;
        }
        report.startReport("Summary client: " + iperf.client.getHost() + " server : " + iperf.server.getHost() + " with buffer length of " + bufferLen, "summary");
        report.addProperty("Best performance", String.valueOf(bestThroughput));
        return bestThroughput;
    }
