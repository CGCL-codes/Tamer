    private void useDesign(ActionEvent ev) {
        mxProxy.setChannelSource(ModelProxy.PARAMSRC_DESIGN);
        MPXMain.PARAM_SRC = mxProxy.getChannelSource();
        syncModelAction.setEnabled(true);
    }
