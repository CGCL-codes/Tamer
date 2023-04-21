    public static void runRules(int foldNumber, String slotName, String algorithmID) {
        getAnalysisEngine();
        TextRulerToolkit.log("Testing Fold Number " + foldNumber + "\t  Slot: " + slotName + "\t  Algorithm: " + algorithmID);
        String inputFolder = foldRootDirectory + foldNumber + "/testing/withouttags/";
        String rulesFile = foldRootDirectory + foldNumber + "/learnResults/" + slotName + "/" + algorithmID + "/results.tm";
        String scriptFile = tempDir + "results.tm";
        File oldScriptFile = new File(scriptFile);
        if (oldScriptFile.exists()) {
            if (!oldScriptFile.delete()) {
                TextRulerToolkit.log("ERROR DELETING OLD SCRIPT FILE: " + scriptFile);
                return;
            }
        }
        if (!new File(rulesFile).exists()) {
            TextRulerToolkit.log("\tSKIPPED, no rules file not found!");
            return;
        }
        try {
            FileUtils.copyFile(new File(rulesFile), new File(tempDir));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String outputFolder1 = foldRootDirectory + foldNumber + "/testing/markedFromRules";
        String outputFolder2 = foldRootDirectory + foldNumber + "/testing/markedFromRules/" + slotName;
        String outputFolder = foldRootDirectory + foldNumber + "/testing/markedFromRules/" + slotName + "/" + algorithmID;
        new File(outputFolder1).mkdir();
        new File(outputFolder2).mkdir();
        new File(outputFolder).mkdir();
        File[] inputFiles = getXMIFileFromFolder(inputFolder);
        for (File inputFile : inputFiles) {
            sharedCAS = TextRulerToolkit.readCASfromXMIFile(inputFile, ae, sharedCAS);
            try {
                ae.process(sharedCAS);
            } catch (AnalysisEngineProcessException e) {
                e.printStackTrace();
                return;
            }
            TextRulerToolkit.writeCAStoXMIFile(sharedCAS, TextRulerToolkit.addTrailingSlashToPath(outputFolder) + "fromRules_" + inputFile.getName());
        }
    }
