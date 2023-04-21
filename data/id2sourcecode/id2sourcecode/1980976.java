    private void writeFeatures(MapContext mapContext, FLyrVect layer, IWriter writer, OracleSpatialDriver reader, Object[] setDataParams) throws DriverIOException {
        PluginServices.cancelableBackgroundExecution(new OracleWriteTask(mapContext, layer, (OracleSpatialWriter) writer, reader, setDataParams));
    }
