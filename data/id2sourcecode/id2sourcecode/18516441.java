    protected RecordModel getAnalysisResultsRecordModel(String[] plasmidNames) {
        TrackerResultsAnalyser ta = new TrackerResultsAnalyser(file);
        AnalysisResults ar = ta.getAnalysisResults();
        ArrayList cnames = new ArrayList();
        for (int i = 0; i < ar.getAnalysedCell().get(0).getAnalysedChannel().size(); i++) {
            cnames.add(ar.getAnalysedCell().get(0).getAnalysedChannel().get(i).getChannelName());
        }
        PlasmidChannelMatchDialog pcmd = new PlasmidChannelMatchDialog();
        String[] channelNames = (String[]) cnames.toArray(new String[cnames.size()]);
        String[] plasmidOrder = pcmd.getPlasmidOrder(channelNames, plasmidNames);
        final RecordModel analysisResultsRecordModel = recordModelFactory.createRecordModel("AnalysisResults");
        for (int i = 0; i < ar.getAnalysedCell().size(); i++) {
            AnalysedCell ac = ar.getAnalysedCell().get(i);
            RecordModel analysedCellRecordModel = recordModelFactory.createRecordModel("AnalysedCell");
            analysedCellRecordModel.setValue("CellName", ac.getCellName(), false);
            if (ac.getPreStimPeriod() != null) {
                analysedCellRecordModel.setValue("PreStimPeriod", ac.getPreStimPeriod().toString(), false);
            }
            for (int j = 0; j < ac.getAnalysedChannel().size(); j++) {
                AnalysedChannel aCh = ac.getAnalysedChannel().get(j);
                RecordModel analysedChannelRecordModel = recordModelFactory.createRecordModel("AnalysedChannel");
                analysedChannelRecordModel.setValue("ChannelName", aCh.getChannelName(), false);
                analysedChannelRecordModel.setValue("PlasmidName", plasmidOrder[j], false);
                for (int k = 0; k < aCh.getPeak().size(); k++) {
                    org.mcisb.beacon.model.Peak peak = aCh.getPeak().get(k);
                    RecordModel peakRecordModel = recordModelFactory.createRecordModel("Peak");
                    peakRecordModel.setValue("time", Double.toString(peak.getTime()), false);
                    peakRecordModel.setValue("value", Double.toString(peak.getValue()), false);
                    peakRecordModel.updateDisplayName();
                    analysedChannelRecordModel.addChild("Peak", peakRecordModel, false);
                    analysedChannelRecordModel.updateDisplayName();
                }
                if (aCh.getMaxPeak() != null) {
                    analysedChannelRecordModel.setValue("MaxPeak", aCh.getMaxPeak().toString(), false);
                }
                if (aCh.getPeriod() != null) {
                    analysedChannelRecordModel.setValue("Period", aCh.getPeriod().toString(), false);
                }
                if (aCh.getPeriodStdDev() != null) {
                    analysedChannelRecordModel.setValue("PeriodStdDev", aCh.getPeriodStdDev().toString(), false);
                }
                if (aCh.getDecayRate() != null) {
                    analysedChannelRecordModel.setValue("DecayRate", aCh.getDecayRate().toString(), false);
                }
                if (aCh.getFirstPeakTime() != null) {
                    analysedChannelRecordModel.setValue("FirstPeakTime", aCh.getFirstPeakTime().toString(), false);
                }
                analysedChannelRecordModel.updateDisplayName();
                analysedCellRecordModel.addChild("AnalysedChannel", analysedChannelRecordModel, false);
            }
            analysisResultsRecordModel.addChild("AnalysedCell", analysedCellRecordModel, false);
            analysisResultsRecordModel.updateDisplayName();
        }
        return analysisResultsRecordModel;
    }
