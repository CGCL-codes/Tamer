    @Override
    public boolean runModel(List<NeissModelParameter<?>> inParams) {
        this.addToMessages("BHPSLinker.runModel() starting\n", false);
        this.populateParameterList();
        this.addToMessages(this.setParameterValues(inParams), false);
        this.regionList = DistrictHash.extractDistricts(this.regionNames.getValue());
        String dataName = DistrictHash.hashDistricts(regionList);
        if (dataName == null || dataName.length() != 4) {
            this.addToMessages("BHPSLinker could not create a data name (hash) from the region(s): " + this.regionNames.toString() + ". Data name is: " + dataName == null ? "null" : dataName, true);
            return false;
        }
        this.addToMessages("BHPSLinker has input region names '" + this.regionNames.getValue() + "', which gives regions " + this.regionList.toString() + "and hash " + dataName, false);
        List<File> toDelete = new ArrayList<File>();
        try {
            File combinedFile = this.linkToBHPS(toDelete, dataName);
            this.addToMessages("Re-reading linked file so it can be aggregated.\n", false);
            StringBuilder msg = new StringBuilder();
            DataSource linkedData = DataSourceFactory.createDataSource(combinedFile.getAbsolutePath(), CSVFile.class, msg);
            if (linkedData == null) {
                this.addToMessages("Problem re-reading combined file. Messages are: " + msg + "\n", true);
                return false;
            }
            msg = new StringBuilder();
            List<List<Object>> data = linkedData.getValues(null, new String[] { AREA_CODE, this.fieldToAggregate.getValue() }, null, null, new String[] { AREA_CODE }, null, msg);
            if (data == null) {
                this.addToMessages("Error reading linked csv file. Messages are: " + msg + "\n", true);
                return false;
            }
            this.addToMessages("Successfully re-read lined file. Now aggregating on " + this.fieldToAggregate.getValue() + "\n", false);
            List<Object> uniqueVals = new ArrayList<Object>();
            for (Object o : data.get(1)) {
                if (!uniqueVals.contains(o)) {
                    uniqueVals.add(o);
                }
            }
            this.addToMessages("Found these unique values: " + uniqueVals.toString() + "\n", false);
            List<List<Object>> aggregatedData = new ArrayList<List<Object>>();
            for (int i = 0; i < uniqueVals.size() + 1; i++) {
                aggregatedData.add(new ArrayList<Object>());
            }
            String code = (String) data.get(0).get(0);
            aggregatedData.get(0).add(code);
            int[] groupTotals = new int[uniqueVals.size()];
            for (int x = 0; x < groupTotals.length; x++) {
                groupTotals[x] = 0;
            }
            for (int i = 0; i < data.get(0).size(); i++) {
                if (!data.get(0).get(i).toString().equals(code)) {
                    for (int x = 0; x < groupTotals.length; x++) {
                        aggregatedData.get(x + 1).add(groupTotals[x]);
                        groupTotals[x] = 0;
                    }
                    code = data.get(0).get(i).toString();
                    aggregatedData.get(0).add(code);
                }
                boolean foundGroup = false;
                for (int x = 0; x < uniqueVals.size(); x++) {
                    if (uniqueVals.get(x).equals(data.get(1).get(i))) {
                        groupTotals[x] += 1;
                        foundGroup = true;
                        break;
                    }
                }
                if (!foundGroup) {
                    this.addToMessages("BHPSLinker internal error: no aggregate group found for " + "value: " + data.get(1).get(i) + ". Groups are: " + uniqueVals.toString(), true);
                    return false;
                }
                if (i == data.get(0).size() - 1) {
                    for (int x = 0; x < groupTotals.length; x++) {
                        aggregatedData.get(x + 1).add(groupTotals[x]);
                        groupTotals[x] = 0;
                    }
                }
            }
            String yr = this.populationFile.getValue().substring(6, 8);
            File aggregatedFile = new File(BHPSLinker.DYNAMIC_MODEL_ROOT_DIR + "/" + dataName + yr + "_" + this.fieldToAggregate.getValue() + "_AGG.csv");
            this.addToMessages("Finished aggregating data, writing it out to: " + aggregatedFile.getAbsolutePath() + "\n", false);
            BufferedWriter w = new BufferedWriter(new FileWriter(aggregatedFile));
            w.write("OA_Code,");
            for (int i = 0; i < aggregatedData.size() - 1; i++) {
                w.write(this.fieldToAggregate.getValue() + "_" + uniqueVals.get(i) + ((i == aggregatedData.size() - 2) ? "" : ","));
            }
            w.write("\n");
            String s = "";
            for (int i = 0; i < aggregatedData.get(0).size(); i++) {
                s = "";
                for (int j = 0; j < aggregatedData.size(); j++) {
                    s += (aggregatedData.get(j).get(i) + ((j == aggregatedData.size() - 1) ? "" : ","));
                }
                w.write(s + "\n");
            }
            w.close();
            File copiedAggFile = new File(BHPSLinker.DATA_DIR + aggregatedFile.getName());
            File copiedCombinedFile = new File(BHPSLinker.DATA_DIR + combinedFile.getName());
            this.addToMessages("BHPSLinker Copying results to user data directory:" + "\n\t" + aggregatedFile.getAbsolutePath() + "->" + copiedAggFile.getAbsolutePath() + "\n\t" + combinedFile.getAbsolutePath() + "->" + copiedCombinedFile.getAbsolutePath(), false);
            FileHelper.copy(aggregatedFile, copiedAggFile);
            FileHelper.copy(combinedFile, copiedCombinedFile);
            toDelete.add(aggregatedFile);
            toDelete.add(combinedFile);
            if (NeissModelMain.saveExternally()) {
                this.addToMessages("BHPSLinker will attempt to save the results file '" + copiedAggFile.getName() + "' externally for user '" + NeissModelMain.getUserName() + "'", false);
                boolean copyAgg = NeissModel.saveResultsExternally(NeissModelMain.getUserName(), xmlDoc, copiedAggFile, "BHPSLinker aggregate file: " + copiedAggFile.getName());
                if (!(copyAgg)) {
                    this.addToMessages("Error: could not save the results externally, see logs " + "for details.\n", true);
                }
            }
            for (NeissModelParameter<?> p : this.parameters) {
                this.xmlDoc.addParameter(p.getName(), p.getType(), p.getValue().toString());
            }
            this.xmlDoc.addResultsInformation(PCONST.AGGREGATED_FILE, copiedAggFile.getName());
            this.xmlDoc.addResultsInformation("Combined Population File " + copiedCombinedFile.getName(), copiedCombinedFile.getName());
        } catch (IOException ex) {
            this.addToMessages("IOException trying to read/write population file or bhps file: " + ex.getMessage() + "\n", true);
            return false;
        }
        this.addToMessages("BHPS linker completed. Removing old data files:\n", false);
        for (File f : toDelete) {
            this.addToMessages("\t" + f.getAbsolutePath() + "\n", false);
            f.delete();
        }
        this.xmlDoc.setSuccessFailure(true);
        return true;
    }
