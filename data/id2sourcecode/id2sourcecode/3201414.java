    public Object getChannelObject() throws XAwareException {
        FileDriverData54 data = new FileDriverData54();
        data.setFileName((String) m_props.get(XAwareConstants.XAWARE_FILE_NAME));
        data.setMode((String) m_props.get(XAwareConstants.BIZCOMPONENT_ATTR_REQUEST_TYPE));
        data.setMediaType((String) m_props.get(FileBizDriver.ATTR_MEDIA_TYPE));
        data.setBufferData((String) m_props.get(FileBizDriver.ATTR_BUFFER_DATA));
        return data;
    }
