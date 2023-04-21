    public Annotation_TaskCreate(MapContext mapContext, Annotation_Layer lyr, IWriter writer, Driver reader, String duplicate) throws ReadDriverException {
        this.duplicate = duplicate;
        this.mapping = lyr.getAnnotatonMapping();
        this.mapContext = mapContext;
        this.lyrVect = lyr;
        this.writer = writer;
        this.reader = (VectorialDriver) reader;
        setInitialStep(0);
        setDeterminatedProcess(true);
        setStatusMessage(PluginServices.getText(this, "exporting_") + ": " + PluginServices.getText(this, "annotations"));
        va = lyrVect.getSource();
        sds = lyrVect.getRecordset();
        bitSet = sds.getSelection();
        if (bitSet.cardinality() == 0) {
            rowCount = va.getShapeCount();
        } else {
            rowCount = bitSet.cardinality();
        }
        setFinalStep(rowCount);
    }
