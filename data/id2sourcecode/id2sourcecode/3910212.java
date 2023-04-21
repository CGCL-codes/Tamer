    public void saveArrivalRateToFile() {
        FileWriter dout = null;
        try {
            dout = new FileWriter(location + "\\" + distrDest);
            dout.write("fun readArrivalRate() = " + CpnUtils.getCpnDistributionFunction(global.getCaseGenerationScheme()) + ";");
            dout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
