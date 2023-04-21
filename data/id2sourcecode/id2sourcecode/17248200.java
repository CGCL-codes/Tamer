    public DSM2ModelDiff(DSM2Model modelBase, DSM2Model modelChanged) {
        addToBase = new DSM2Model();
        deleteFromBase = new DSM2Model();
        changeToBase = new DSM2Model();
        diff(modelBase.getChannels(), modelChanged.getChannels());
        diff(modelBase.getNodes(), modelChanged.getNodes());
        diff(modelBase.getReservoirs(), modelChanged.getReservoirs());
        diff(modelBase.getGates(), modelChanged.getGates());
        diff(modelBase.getInputs(), modelChanged.getInputs());
        diff(modelBase.getOutputs(), modelChanged.getOutputs());
    }
