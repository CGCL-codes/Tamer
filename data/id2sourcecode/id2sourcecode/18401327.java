    public FileTemplate54(FileBizDriver driver) throws XAwareException {
        FileDriverData54 data = (FileDriverData54) driver.createChannelObject();
        fileName = data.getFileName();
        mediaType = data.getMediaType();
        bufferData = data.getBufferData();
        String modeStr = data.getMode();
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
