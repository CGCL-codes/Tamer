    private void saveSettingsToFile() {
        FileWriter cout1 = null;
        FileWriter cout2 = null;
        FileWriter rout = null;
        FileWriter dout = null;
        try {
            cout1 = new FileWriter(location + "\\" + caseDest);
            cout1.write("fun readNumberOfCases() = " + (int) numberOfCases.getValue() + ";");
            cout1.close();
            cout2 = new FileWriter(location + "\\" + caseRead);
            cout2.write("" + (int) numberOfCases.getValue());
            cout2.close();
            rout = new FileWriter(location + "\\" + runDest);
            rout.write("" + (int) numberOfSubRuns.getValue());
            rout.close();
            dout = new FileWriter(location + "\\" + distrDest);
            dout.write("fun readArrivalRate() = " + CpnUtils.getCpnDistributionFunction(global.getCaseGenerationScheme()) + ";");
            dout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
