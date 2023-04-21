    public void dumpData(XmlDataAdaptor da) {
        da.setValue("name", name);
        da.setValue("xPV", wrpChX.getChannelName());
        da.setValue("yPV", wrpChY.getChannelName());
        da.setValue("isActive", isActive.booleanValue());
    }
