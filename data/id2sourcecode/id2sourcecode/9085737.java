    public DataList call(DataList parameters, int moduleId) throws ECallFailure {
        DataBlockWriter writer = null;
        DataBlockReader reader = null;
        try {
            writer = writeData((SBWDataList) parameters);
            reader = toSbw.call(moduleId, serviceId, methodId, writer);
        } catch (SBWException e) {
            throw new ECallFailure(e.toString());
        } finally {
            writer.release();
        }
        return readData(reader);
    }
