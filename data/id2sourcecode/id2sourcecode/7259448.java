    private void useCa(ActionEvent ev) {
        mxProxy.setChannelSource(ModelProxy.PARAMSRC_LIVE);
        MPXMain.PARAM_SRC = mxProxy.getChannelSource();
        syncModelAction.setEnabled(true);
    }
