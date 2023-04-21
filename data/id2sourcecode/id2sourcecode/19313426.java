    public void updateDataSetOnGraphPanel() {
        graphAnalysis.removeAllGraphData();
        graphAnalysis.clearZoomStack();
        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
            if (measuredValuesShowStateV.get(i).booleanValue()) {
                MeasuredValue mv_tmp = measuredValuesV.get(i);
                if (scanPV_ShowState || scanVariable.getChannel() == null) {
                    graphAnalysis.addGraphData(mv_tmp.getDataContainers());
                }
                if (scanPV_RB_ShowState) {
                    graphAnalysis.addGraphData(mv_tmp.getDataContainersRB());
                }
            }
        }
        graphAnalysis.addGraphData(graphDataLocal);
        if (indexOfPanelNew < analysisControllers.length) {
            analysisControllers[indexOfPanelNew].updateDataSetOnGraphPanel();
        }
    }
