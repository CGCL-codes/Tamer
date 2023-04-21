    public void writeFeaturesNoThread(FLyrVect layer, IWriter writer) throws ReadDriverException, VisitorException, ExpansionFileReadException {
        ReadableVectorial va = layer.getSource();
        SelectableDataSource sds = layer.getRecordset();
        writer.preProcess();
        int rowCount;
        FBitSet bitSet = layer.getRecordset().getSelection();
        if (bitSet.cardinality() == 0) rowCount = va.getShapeCount(); else rowCount = bitSet.cardinality();
        ProgressMonitor progress = new ProgressMonitor((JComponent) PluginServices.getMDIManager().getActiveWindow(), PluginServices.getText(this, "exportando_features"), PluginServices.getText(this, "exportando_features"), 0, rowCount);
        progress.setMillisToDecideToPopup(200);
        progress.setMillisToPopup(500);
        if (bitSet.cardinality() == 0) {
            rowCount = va.getShapeCount();
            for (int i = 0; i < rowCount; i++) {
                IGeometry geom = va.getShape(i);
                progress.setProgress(i);
                if (progress.isCanceled()) break;
                if (geom != null) {
                    Value[] values = sds.getRow(i);
                    IFeature feat = new DefaultFeature(geom, values, "" + i);
                    DefaultRowEdited edRow = new DefaultRowEdited(feat, IRowEdited.STATUS_ADDED, i);
                    writer.process(edRow);
                }
            }
        } else {
            int counter = 0;
            for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
                IGeometry geom = va.getShape(i);
                progress.setProgress(counter++);
                if (progress.isCanceled()) break;
                if (geom != null) {
                    Value[] values = sds.getRow(i);
                    IFeature feat = new DefaultFeature(geom, values, "" + i);
                    DefaultRowEdited edRow = new DefaultRowEdited(feat, IRowEdited.STATUS_ADDED, i);
                    writer.process(edRow);
                }
            }
        }
        writer.postProcess();
        progress.close();
    }
