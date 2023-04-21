    private void writeFeatures(MapContext mapContext, FLyrVect layer, IWriter writer, Driver reader) throws ReadDriverException {
        PluginServices.cancelableBackgroundExecution(new WriterTask(mapContext, layer, writer, reader));
    }
