    public FileTemplate(FileBizDriver driver) throws XAwareException {
        driver.createChannelObject();
        fileName = driver.getData().getFileName();
        String modeStr = driver.getData().getMode();
        if (XAwareConstants.XAWARE_FILE_READ.equals(modeStr)) {
            mode = XAwareConstants.FileMode.READ;
        } else if (XAwareConstants.XAWARE_FILE_WRITE.equals(modeStr)) {
            mode = XAwareConstants.FileMode.WRITE;
        } else if (XAwareConstants.XAWARE_FILE_APPEND.equals(modeStr)) {
            mode = XAwareConstants.FileMode.APPEND;
        } else {
            throw new XAwareException("FileTemplate: " + XAwareConstants.BIZCOMPONENT_ATTR_REQUEST_TYPE + " must be \"read\", \"write\" or \"append\"");
        }
    }
