    public void addData(AbstractHardwareData data) {
        logger.debug("Adding data to custom table model...");
        Vector dataVector = new Vector();
        dataVector.add(data.getHardwareId());
        dataVector.add(data.getDataString());
        dataVector.add(data.getTimestamp());
        dataVector.add(data.getChannel());
        tableModel.addRow(dataVector);
    }
