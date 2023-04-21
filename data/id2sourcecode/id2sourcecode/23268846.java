    public void firePickChange(int objectNumber) {
        PickChangeListener.DataPack dataPack = new PickChangeListener.DataPack();
        Molecule molecule = model.getMolecule(objectNumber);
        if (molecule != null) {
            dataPack.structure = molecule;
            dataPack.xTitle = model.getChannelTitle(X_CHANNEL);
            if (dataPack.xTitle.isEmpty()) {
                dataPack.xTitle = null;
            } else {
                dataPack.xValue = model.getData(X_CHANNEL, objectNumber);
            }
            dataPack.yTitle = model.getChannelTitle(Y_CHANNEL);
            if (dataPack.yTitle.isEmpty()) {
                dataPack.yTitle = null;
            } else {
                dataPack.yValue = model.getData(Y_CHANNEL, objectNumber);
            }
            dataPack.zTitle = model.getChannelTitle(Z_CHANNEL);
            if (dataPack.zTitle.isEmpty()) {
                dataPack.zTitle = null;
            } else {
                dataPack.zValue = model.getData(Z_CHANNEL, objectNumber);
            }
            dataPack.colorTitle = model.getChannelTitle(COLOR_CHANNEL);
            if (dataPack.colorTitle.isEmpty()) {
                dataPack.colorTitle = null;
            } else {
                dataPack.colorValue = model.getData(COLOR_CHANNEL, objectNumber);
            }
            dataPack.sizeTitle = model.getChannelTitle(SIZE_CHANNEL);
            if (dataPack.sizeTitle.isEmpty()) {
                dataPack.sizeTitle = null;
            } else {
                dataPack.sizeValue = model.getData(SIZE_CHANNEL, objectNumber);
            }
        }
        for (PickChangeListener listener : pickChangeListenerList) {
            listener.pickChanged(dataPack);
        }
    }
