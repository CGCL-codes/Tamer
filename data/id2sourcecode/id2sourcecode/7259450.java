    private void useRfDesign(ActionEvent ev) {
        mxProxy.setChannelSource(ModelProxy.PARAMSRC_RF_DESIGN);
        MPXMain.PARAM_SRC = mxProxy.getChannelSource();
        syncModelAction.setEnabled(true);
    }
