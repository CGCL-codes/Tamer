        public WriterTask(MapContext mapContext, FLyrVect lyr, IWriter writer, Driver reader) throws ReadDriverException {
            this.mapContext = mapContext;
            this.lyrVect = lyr;
            this.writer = writer;
            this.reader = (VectorialDriver) reader;
            setInitialStep(0);
            setDeterminatedProcess(true);
            setStatusMessage(PluginServices.getText(this, "exportando_features"));
            va = lyrVect.getSource();
            sds = lyrVect.getRecordset();
            bitSet = sds.getSelection();
            if (bitSet.cardinality() == 0) rowCount = va.getShapeCount(); else rowCount = bitSet.cardinality();
            setFinalStep(rowCount);
        }
