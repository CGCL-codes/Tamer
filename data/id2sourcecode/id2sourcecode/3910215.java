    public void copySettingsToOneFile(String fileName) {
        FileWriter out = null;
        try {
            out = new FileWriter(fileName);
            out.write("The simulation settings of this experiment are: \n");
            out.write("The number of cases: " + (int) readCaseFromFile() + "\n");
            out.write("The number of sub runs: " + (int) readRunFromFile() + "\n");
            out.write("The arrival rate: " + CpnUtils.getCpnDistributionFunction(global.getCaseGenerationScheme()) + "\n");
            out.close();
        } catch (IOException excep) {
            excep.printStackTrace();
        }
    }
