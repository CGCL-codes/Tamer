    public void createSettingFiles(String simDest, HLDistribution d) {
        String caseLoc = simDest + "\\" + caseDest;
        File caseFile = new File(caseLoc);
        if (!caseFile.exists()) {
            try {
                caseFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileWriter cout = null;
        try {
            cout = new FileWriter(caseLoc);
            cout.write("fun readNumberOfCases() = 100;");
            cout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String case2Loc = simDest + "\\" + caseRead;
        try {
            (new File(case2Loc)).createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileWriter c2out = null;
        try {
            c2out = new FileWriter(case2Loc);
            c2out.write("" + (int) 100);
            c2out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String runLoc = simDest + "\\" + runDest;
        try {
            (new File(runLoc)).createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileWriter rout = null;
        try {
            rout = new FileWriter(runLoc);
            rout.write("" + (int) 3);
            rout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String distrLoc = simDest + "\\" + distrDest;
        try {
            (new File(distrLoc)).createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileWriter dout = null;
        try {
            dout = new FileWriter(distrLoc);
            dout.write("fun readArrivalRate() = " + CpnUtils.getCpnDistributionFunction(global.getCaseGenerationScheme()) + ";");
            dout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String locLoc = simDest + "\\" + locDest;
        try {
            (new File(locLoc)).createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileWriter lout = null;
        try {
            lout = new FileWriter(locLoc);
            lout.write("val FOLDER = \"C:/RedesignAnalysis/experiment_1/Original_0/sim_1\"");
            lout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
