    protected boolean handleManualMapping(final String className, final String readHandler, final String writeHandler) throws ObjectDescriptionException {
        final ManualMappingInfo manualMappingInfo = new ManualMappingInfo(loadClass(className), loadClass(readHandler), loadClass(writeHandler));
        manualMappingInfo.setComments(new Comments(getOpenComment(), getCloseComment()));
        manualMappingInfo.setSource(this.source);
        this.model.getMappingModel().addManualMapping(manualMappingInfo);
        return true;
    }
