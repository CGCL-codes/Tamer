    private void writeFeatures(MapContext mapContext, Annotation_Layer layer, IWriter writer, Driver reader, String duplicate) throws ReadDriverException {
        PluginServices.cancelableBackgroundExecution(new Annotation_TaskCreate(mapContext, layer, writer, reader, duplicate));
    }
