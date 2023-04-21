    private void writeMultiFeatures(MapContext mapContext, FLyrVect layers, IWriter[] writers, Driver[] readers) throws ReadDriverException {
        MultiWriterTask mwt = new MultiWriterTask();
        for (int i = 0; i < writers.length; i++) {
            mwt.addTask(new WriterTask(mapContext, layers, writers[i], readers[i]));
        }
        PluginServices.cancelableBackgroundExecution(mwt);
    }
